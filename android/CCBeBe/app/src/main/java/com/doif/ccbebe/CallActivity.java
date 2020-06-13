package com.doif.ccbebe;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.webrtc.PeerConnectionAdapter;
import com.doif.ccbebe.webrtc.SdpAdapter;
import com.doif.ccbebe.webrtc.SignalingClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class CallActivity extends AppCompatActivity implements SignalingClient.Callback {

    private String TAG = "[CallActSend] ";

//    private WebView ccbebe_webview;
//    private WebSettings ccbebe_webSettings;

//    private String serverUrl;
//    private String apiPath;
//    private String userId;
//    private String isStreamer;

    private EglBase.Context eglBaseContext;
    private PeerConnectionFactory peerConnectionFactory;

    private MediaStream mediaStream;
    private List<PeerConnection.IceServer> iceServers;
    private HashMap<String, PeerConnection> peerConnectionHashMap;

    private SurfaceTextureHelper surfaceTextureHelper;
    private SurfaceViewRenderer callView;

    private String[] permissions = new String[] {Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO};
    private String stunServerUrl = "stun:stun.l.google.com:19302";
    private String turnServerUrl = "turn:54.180.166.183";

    private String userId;
    private String isStreamer;
    private String serverUrl;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        if(hasPermissions()){
            initialization();
        }else{
            requestPermissions();
        }

    }

    /**
     * initialize
     * */
    private void initialization(){

        // initialize
        peerConnectionHashMap = new HashMap<>();

        iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder(stunServerUrl).createIceServer());
        iceServers.add(PeerConnection.IceServer.builder(turnServerUrl).setUsername("test").setPassword("test").createIceServer());

        eglBaseContext = EglBase.create().getEglBaseContext();

        // bind view
        callView = findViewById(R.id.callView);

        // set view
        callView.setMirror(true);
        callView.init(eglBaseContext, null);

        // get user information
        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        _id = sharedPreferences.getString("_id", null);
        userId = sharedPreferences.getString("userId", null);
        isStreamer = sharedPreferences.getString("isStreamer", "false");

        // set SignalingServer information
        SignalingClient.isStreamer = isStreamer.equals("true") ? true : false;
        SignalingClient.serverUrl = serverUrl;
        SignalingClient.monitoringId = userId;
        SignalingClient.streamerId = userId + "-camera";

        // create PeerConnectionFactory
        PeerConnectionFactory.InitializationOptions initializationOptions = PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBaseContext);

        peerConnectionFactory = PeerConnectionFactory.builder()
                                .setOptions(options)
                                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                                .createPeerConnectionFactory();

        surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext);

        // create AudioSource
        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        AudioTrack audioTrack = peerConnectionFactory.createAudioTrack("100", audioSource);

        // create VideoCapturer
        VideoCapturer videoCapturer = createCameraCapturer();
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), videoSource.getCapturerObserver());
        videoCapturer.startCapture(480, 640, 30);

        // create VideoTrack
        VideoTrack videoTrack = peerConnectionFactory.createVideoTrack("101", videoSource);

        // create MediaStream
        mediaStream = peerConnectionFactory.createLocalMediaStream("102");

        mediaStream.addTrack(audioTrack);
        mediaStream.addTrack(videoTrack);

        if(isStreamer.equals("true")) videoTrack.addSink(callView);

        SignalingClient.get().init(this);
    }

    /**
     * PeerConnection
     * */
    private synchronized PeerConnection getOrCreatepeerConnection(String socketId){

        PeerConnection peerConnection = peerConnectionHashMap.get(socketId);

        if(peerConnection != null) {
            return peerConnection;
        }

        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new PeerConnectionAdapter("PC:" + socketId) {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);

                Log.d(TAG, "onIceCandidate");

                SignalingClient.get().sendIceCandidate(iceCandidate, socketId);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                Log.d(TAG, "onAddStream");

                if(isStreamer.equals("false")){

                    runOnUiThread(() -> {
                        VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
                        remoteVideoTrack.addSink(callView);
                    });

                }

            }
        });

        peerConnection.addStream(mediaStream);

        peerConnectionHashMap.put(socketId, peerConnection);

        return peerConnection;
    }

    /**
     * Signaling server
     * */
    @Override
    public void onPeerJoined(String socketId){
        Log.d(TAG, "onPeerJoined");

        PeerConnection peerConnection = getOrCreatepeerConnection(socketId);
        peerConnection.createOffer(new SdpAdapter("createOfferSdp : " + socketId){
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription){
                super.onCreateSuccess(sessionDescription);
                Log.d(TAG, "onPeerJoined - createOffer");

                peerConnection.setLocalDescription(new SdpAdapter("setLocalSdp: " + socketId), sessionDescription);
                SignalingClient.get().sendSessionDescription(sessionDescription, socketId);

            }
        }, new MediaConstraints());
    }

    @Override
    public void onOfferReceived(JSONObject data){

        Log.d(TAG, "onOfferReceived");

        runOnUiThread(() -> {
            final String socketId = data.optString("from");

            PeerConnection peerConnection = getOrCreatepeerConnection(socketId);

            try{
                peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp: " + socketId),
                        new SessionDescription(SessionDescription.Type.OFFER, data.getJSONObject("sdp").optString("sdp")));
            }catch (JSONException e){
                Log.d(TAG, "onOfferReceived - json exception is occurs" );
            }

            peerConnection.createAnswer(new SdpAdapter("localAnswerSdp"){
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription){
                    super.onCreateSuccess(sessionDescription);

                    Log.d(TAG, "onOfferReceived - onCreateSuccess");

                    peerConnectionHashMap.get(socketId).setLocalDescription(new SdpAdapter("setLocalSdp : " + socketId), sessionDescription);

                    SignalingClient.get().sendSessionDescription(sessionDescription, socketId);

                }
            }, new MediaConstraints());
        });
    }

    @Override
    public void onAnswerReceived(JSONObject data){
        Log.d(TAG, "onAnswerReceived");

        String socketId = data.optString("from");
        PeerConnection peerConnection = getOrCreatepeerConnection(socketId);

        try{
            peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp: " + socketId),
                    new SessionDescription(SessionDescription.Type.ANSWER, data.getJSONObject("sdp").optString("sdp")));
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onIceCandidateReceived(JSONObject data){

        Log.d(TAG, "onIceCandidateReceived - " + data);

        String socketId = data.optString("from");

        PeerConnection peerConnection = getOrCreatepeerConnection(socketId);

        try{
            JSONObject candidateObject = data.getJSONObject("candidate");
            peerConnection.addIceCandidate(new IceCandidate(candidateObject.optString("sdpMid"),
                                                            candidateObject.optInt("sdpMLineIndex"),
                                                            candidateObject.optString("candidate")));
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SignalingClient.get().endRoom();

        Set<String> keySet = peerConnectionHashMap.keySet();

        for(String key : keySet){
            PeerConnection peerConnection = peerConnectionHashMap.get(key);
            peerConnection.close();
        }
    }

    /**
     * create camera capturer
     * */
    private VideoCapturer createCameraCapturer(){
        Camera1Enumerator enumerator = new Camera1Enumerator(false);
        final String[] deviceNames = enumerator.getDeviceNames();

        // try to find back facing camera
        for (String deviceName : deviceNames){
            if(enumerator.isBackFacing(deviceName)){
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if(videoCapturer != null) return videoCapturer;
            }
        }

        // Back facing camera not found, try something else
        for (String deviceName : deviceNames){
            if(!enumerator.isBackFacing(deviceName)){
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if(videoCapturer != null) return videoCapturer;
            }
        }

        return null;
    }

    /**
     * permission
     * */
    private boolean hasPermissions(){

        int res = 0;

        for(String permission : permissions){
            res = checkCallingOrSelfPermission(permission);

            if(! (res == PackageManager.PERMISSION_GRANTED))
                return false;
        }
        return true;
    }

    private void requestPermissions(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        boolean allowed = true;

        switch (requestCode){
            case 0:
                for(int res : grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }

        if(allowed){
            initialization();
        }else{
            Toast.makeText(getApplicationContext(), "설정에서 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * web view
     * */
//
//    private void initialization(){
//
//        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
//        userId = sharedPreferences.getString("userId", null);
//        isStreamer = sharedPreferences.getString("isStreamer", "false");
//
//        serverUrl = "https://ccbebe.io";
//        apiPath = "?id=" + userId + "&isStreamer=" + isStreamer;
//        serverUrl = serverUrl + apiPath;
//
//        Log.d(TAG, serverUrl);
//
//        // set webview
//        ccbebe_webview = (WebView) findViewById(R.id.ccbebe_webview);
//
//        ccbebe_webview.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                //동작
//                ccbebe_webview.setWebViewClient(new WebViewClient());
//                ccbebe_webSettings = ccbebe_webview.getSettings();
//                ccbebe_webSettings.setJavaScriptEnabled(true);
//
//                ccbebe_webSettings.setAllowFileAccessFromFileURLs(true);
//                ccbebe_webSettings.setAllowUniversalAccessFromFileURLs(true);
//                ccbebe_webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//                ccbebe_webSettings.setJavaScriptEnabled(true);
//                ccbebe_webSettings.setDomStorageEnabled(true);
//                ccbebe_webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//                ccbebe_webSettings.setBuiltInZoomControls(true);
//                ccbebe_webSettings.setAllowFileAccess(true);
//                ccbebe_webSettings.setSupportZoom(true);
//
//
//                ccbebe_webview.setWebChromeClient(new WebChromeClient() {
//
//                    @Override
//                    public void onPermissionRequest(final PermissionRequest request) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            request.grant(request.getResources());
//                        }
//                    }
//
//                });
//
//                ccbebe_webview.loadUrl(serverUrl);
//            }
//        });
//
//    }
//
//    private class MyBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mailto:") || url.startsWith("mms:") || url.startsWith("mmsto:") || url.startsWith("market:") || url.equals("http://wingcrony.com/?actie=donate")) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
//            else {
//                view.loadUrl(url);
//                return true;
//            }
//        }
//    }

}

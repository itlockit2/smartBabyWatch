package com.doif.ccbebe.webrtc;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SignalingClient {

    private String TAG = "[SignalingCT] ";

    private static SignalingClient instance;

    public static String serverUrl;
    public static String userId;
    public static String streamerId;
    public static String monitoringId;

    public static boolean isStreamer; // streamer : true / monitoring : false
    static boolean isMedaiServer; //

    private Socket socket;
    private Callback callback;

    public static SignalingClient get(){
        if(instance == null){
            synchronized (SignalingClient.class){
                if(instance == null){
                    instance = new SignalingClient();
                }
            }
        }

        return instance;
    }

    private final TrustManager[] trustAll = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };

    public void init(Callback callback){

        Log.d(TAG, "init");

        try{

            this.callback = callback;

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAll, null);
            IO.setDefaultHostnameVerifier((hostname, session) -> true);
            IO.setDefaultSSLContext(sslContext);

            socket = IO.socket(serverUrl);
            socket.connect();

            if(isStreamer){
                shoot();
            }else{
                monitor();
            }

        }catch (NoSuchAlgorithmException e){
            Log.d(TAG, "NoSuchAlgorithmException is occurs");
        }catch (KeyManagementException e){
            Log.d(TAG, "NoSuchAlgorithmException is occurs");
        }catch (URISyntaxException e){
            Log.d(TAG, "URISyntaxException is occurs");
        }catch(Exception e){
            Log.d(TAG, e.getMessage());
        }

    }

    /**
     * shooting
     * */

    private void shootInit(){
        // init
        socket.emit("init", streamerId);
        Log.d(TAG, "init");

        socket.on("init", args->{
            Log.d(TAG, "init - " + args[0]);
        });

        // request
        socket.emit("createStreamRoom", streamerId);
        Log.d(TAG, "createStreamRoom");

        shoot();
    }

    public void shoot(){

        // init
        socket.emit("init", streamerId);
        Log.d(TAG, "init");

        socket.on("init", args->{
            Log.d(TAG, "init - " + args[0]);
        });

        // request
        socket.emit("createStreamRoom", streamerId);
        Log.d(TAG, "createStreamRoom");

        socket.on("request", args -> {
            Log.d(TAG, "request - " + args[0]);

            try{
                JSONObject jsonObject = (JSONObject) args[0];
                String socketId = jsonObject.getString("from");

                Log.d(TAG, "request - " + socketId);

                isMedaiServer = socketId.equals("mediaServer") ? true:false;
                callback.onPeerJoined(socketId);

            }catch (JSONException e){
                Log.d(TAG, "request - json exception is occurs");
            }
        });

        // call
        socket.on("call", args->{
            Log.d(TAG, "call - " +args[0]);

            JSONObject jsonObject = (JSONObject) args[0];

            try{

                if(jsonObject.getJSONObject("sdp").getString("type").equals("offer"))
                    callback.onOfferReceived(jsonObject);
                else if(jsonObject.getJSONObject("sdp").getString("type").equals("answer"))
                    callback.onAnswerReceived(jsonObject);
            }catch (JSONException e){
                callback.onIceCandidateReceived(jsonObject);
            }
        });

    }

    /**
     * monitoring
     * */
    public void monitor(){
        // init
        socket.emit("init", monitoringId);
        socket.on("init", args -> {
            Log.d(TAG, "init - " + args[0]);
        });

        // room
        socket.emit("room", streamerId);

        // request
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", streamerId);
            jsonObject.put("from", monitoringId);

            socket.emit("request", jsonObject);

            Log.d(TAG, "request - " + jsonObject);
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }

        // call
        socket.on("call", args-> {

            Log.d(TAG, "call - " + args[0]);

            JSONObject jsonObject = (JSONObject) args[0];

            try{


                if(jsonObject.getJSONObject("sdp").getString("type").equals("offer"))
                    callback.onOfferReceived(jsonObject);
                else if (jsonObject.getJSONObject("sdp").getString("type").equals("answer"))
                    callback.onAnswerReceived(jsonObject);
            }catch (JSONException e){
                Log.d(TAG, e.getMessage() + " - " + jsonObject);
                callback.onIceCandidateReceived(jsonObject);
            }
        });
    }


    /**
     * send sdp
     * */
    public void sendSessionDescription(SessionDescription sdp, String socketId){

        JSONObject jsonObject = new JSONObject();
        JSONObject sdpObject = new JSONObject();

        String toId = socketId;
        String fromId = isStreamer ? streamerId : monitoringId;

        try{

            sdpObject.put("type", sdp.type.canonicalForm());
            sdpObject.put("sdp", sdp.description);

            jsonObject.put("to", toId);
            jsonObject.put("from", fromId);
            jsonObject.put("sdp", sdpObject);

            socket.emit("call", jsonObject);

            Log.d(TAG, "send sdp - " + jsonObject);

        }catch(JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void sendIceCandidate(IceCandidate iceCandidate, String socketId){

        JSONObject jsonObject = new JSONObject();
        JSONObject candidateObejct = new JSONObject();

        String toId = socketId;
        String fromId = isStreamer ? streamerId : monitoringId;
        try{
            candidateObejct.put("candidate", iceCandidate.sdp);
            candidateObejct.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            candidateObejct.put("sdpMid", iceCandidate.sdpMid);

            jsonObject.put("to", toId);
            jsonObject.put("from", fromId);
            jsonObject.put("candidate", candidateObejct);

            socket.emit("call", jsonObject);

            Log.d(TAG, "sendIceCandidate - " + jsonObject);

        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void sendNullIceCandidate(){

        try{
            String toId = isMedaiServer ? "mediaServer" : monitoringId;

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("to", toId);
            jsonObject.put("from", streamerId);
            jsonObject.put("candidate", JSONObject.NULL);

            socket.emit("call", jsonObject);

            Log.d(TAG, "sendNullIceCandidate - " + jsonObject);

        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void endRoom(){

        if(isStreamer){
            socket.emit("endRoom", streamerId);
            socket.disconnect();
            socket.close();
            Log.d(TAG, "endRoom - " + streamerId);

        }else{
            socket.emit("end", monitoringId);
            socket.disconnect();
            socket.close();
            Log.d(TAG, "end - " + monitoringId);
        }
    }

    public interface Callback{
        void onPeerJoined(String msg);
        void onOfferReceived(JSONObject data);
        void onAnswerReceived(JSONObject data);
        void onIceCandidateReceived(JSONObject data);
    }
}

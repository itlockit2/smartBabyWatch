package com.doif.ccbebe.webrtc;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

public class PeerConnectionAdapter implements PeerConnection.Observer {

    private String TAG = "[PeerConnAdt] ";

    public PeerConnectionAdapter(String s){
        Log.d(TAG, s);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState){
        Log.d(TAG, "onSignalingChange - "  + signalingState);
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState){

        Log.d(TAG, "onIceConnectionChange - " + iceConnectionState);

        if(iceConnectionState == PeerConnection.IceConnectionState.CONNECTED)
            SignalingClient.get().sendNullIceCandidate();
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b){
        Log.d(TAG, "onIceConnectionReceivingChange - " + b);

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState){
        Log.d(TAG, "onIceGatheringChange - " + iceGatheringState);
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate){
        Log.d(TAG, "onIceCandidate - " + iceCandidate);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates){
        Log.d(TAG, "onIceCandidatesRemoved - " +iceCandidates);
    }

    @Override
    public void onAddStream(MediaStream mediaStream){
        Log.d(TAG, "onAddStream - " + mediaStream);
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream){
        Log.d(TAG, "onRemoveStream - " + mediaStream);
    }

    @Override
    public void onDataChannel(DataChannel dataChannel){
        Log.d(TAG, "onDataChannel - " + dataChannel);
    }

    @Override
    public void onRenegotiationNeeded(){
        Log.d(TAG, "onRenegotiationNeeded ");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams){
        Log.d(TAG, "onAddTrack " + mediaStreams);
    }

}

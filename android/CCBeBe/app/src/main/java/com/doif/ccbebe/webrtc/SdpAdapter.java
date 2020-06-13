package com.doif.ccbebe.webrtc;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class SdpAdapter implements SdpObserver {

    private String TAG = "[SdpAdt] " ;

    public SdpAdapter(String s){
        Log.d(TAG, s);
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription){
        Log.d(TAG, "onCreateSuccess -  "  +sessionDescription);
    }

    @Override
    public void onSetSuccess(){
        Log.d(TAG, "onSetSuccess");
    }

    @Override
    public void onCreateFailure(String s){
        Log.d(TAG, "onCrateFailure -  " + s );
    }

    @Override
    public void onSetFailure(String s){
        Log.d(TAG, "onSetFailure - " + s);
    }

}


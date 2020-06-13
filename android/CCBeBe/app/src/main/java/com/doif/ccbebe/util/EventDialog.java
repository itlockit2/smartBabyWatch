package com.doif.ccbebe.util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doif.ccbebe.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.otaliastudios.zoom.ZoomSurfaceView;

import org.jetbrains.annotations.NotNull;

public class EventDialog extends Dialog {

    private String TAG = "[EventDlg] ";

    private Context context;
    private DisplayMetrics metrics;

    // video url
    private String eventTime;
    private String eventStatus;
    private String eventUrl;

    // container
    LinearLayout event_container;

    // video view
    private ZoomSurfaceView event_video_surfaceview;
    private SimpleExoPlayer player;

    // event description
    private TextView event_dialog_time;
    private TextView event_dialog_status;
    private ImageButton event_dialog_close_button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //다이얼로그 밖의 화면은 흐리게 만들어줌
        getWindow().setBackgroundDrawable(new ColorDrawable(0x700000));
        setContentView(R.layout.dialog_event);

        initialize();

    }

    private void initialize(){

        Log.d(TAG, "initialize");
        // bind view
        event_video_surfaceview = findViewById(R.id.event_video_surfaceview);
        event_dialog_time = findViewById(R.id.event_dialog_time);
        event_dialog_status = findViewById(R.id.event_dialog_status);
        event_dialog_close_button = findViewById(R.id.event_dialog_close_button);
        event_container = findViewById(R.id.event_dialog_container);

        // set entire size
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) event_container.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        event_container.setLayoutParams(params);

        // set description
        event_dialog_time.setText(eventTime);
        event_dialog_status.setText(eventStatus);

        // attach listener
        event_dialog_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.release();
                dismiss();
            }
        });


        final boolean supportsSurfaceView = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        if (supportsSurfaceView) setUpVideoPlayer();

        if (supportsSurfaceView) {
            player.setPlayWhenReady(true);
        }
    }

    //생성자 생성
    public EventDialog(Context context, DisplayMetrics metrics, String eventTime, String eventStatus, String eventUrl) {
        super(context);

        this.context = context;
        this.metrics = metrics;
        this.eventTime = eventTime;
        this.eventStatus = eventStatus;
        this.eventUrl = eventUrl;

        Log.d(TAG, eventUrl);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ZoomSurfaceView surface = findViewById(R.id.event_video_surfaceview);
        surface.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        ZoomSurfaceView surface = findViewById(R.id.event_video_surfaceview);
        surface.onResume();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setUpVideoPlayer() {

        player = ExoPlayerFactory.newSimpleInstance(context);

        final PlayerControlView controls = findViewById(R.id.event_video_controlview);
        final ZoomSurfaceView surface = findViewById(R.id.event_video_surfaceview);

        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                surface.setContentSize(width, height);
            }
        });

        surface.addCallback(new ZoomSurfaceView.Callback() {
            @Override
            public void onZoomSurfaceCreated(@NotNull ZoomSurfaceView view) {
                player.setVideoSurface(view.getSurface());
            }

            @Override
            public void onZoomSurfaceDestroyed(@NotNull ZoomSurfaceView view) { }
        });

        controls.setPlayer(player);
        controls.hide();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "ZoomLayoutLib"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(eventUrl));
        player.prepare(videoSource);

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                controls.show();
                return false;
            }
        });

    }

}

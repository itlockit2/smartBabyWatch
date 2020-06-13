package com.doif.ccbebe.fcm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.doif.ccbebe.MainActivity;
import com.doif.ccbebe.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG="[FCMService] ";

    private String appToken;
    // fcm 전송 내용
    private String title;
    private String content;
    private String imageUrl;
    private String clickAction;

    private Bitmap image;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        // 화면 깨우기
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myapp:mytag");
        wakeLock.acquire(3000);

        // 받은 메세지 분류하여 notification 전송
        title = remoteMessage.getData().get("title");
        content = remoteMessage.getData().get("content");
        imageUrl = remoteMessage.getData().get("imageUrl");
        clickAction = remoteMessage.getData().get("clickAction");

        sendNotification(title, content, imageUrl, clickAction);
    }

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);

        // 생성된 토큰 저장
        appToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "appToken - " + appToken);
        //  토큰 값이 null 이 아닐 경우 SharedPreferences에 저장
        if(appToken != null){
            SharedPreferences auto = getSharedPreferences("ccbebe", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = auto.edit();

            editor.putString("appToken", appToken);
            Log.d(TAG, "appToken is saved");

            editor.commit();
        }
    }

    // notification 전송
    private void sendNotification(String title, String messageBody,String imageUrl, String click_action){

        // clickAction 에 따라 알림 클릭시 표시되는 activity 설정
        Intent intent;
        Class nextClass = MainActivity.class;
        switch(click_action){
            case "MainActivity":
                nextClass = MainActivity.class;
                break;
        }
        intent = new Intent(this, nextClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 이미지 -> 비트맵
        try{
            URL url = new URL(imageUrl);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (IOException e){
            Log.e(TAG, "IOException is occurred on loading url", e);
        }
        // 알림 소리 설정
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // 알림 진동 설정
        long[] vibratePattern = {0, 200, 200, 200};

        // 알림 보내기 위한 NotificationgManaver
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // Android version 8.0 이상인 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelName = getString(R.string.default_notification_channel_name);

            Log.d("channelName " , channelName);
            NotificationChannel channel = new NotificationChannel("News", channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "News")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(vibratePattern)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .setBigContentTitle(title)
                        .setSummaryText(messageBody));

        notificationManager.notify(0, notificationBuilder.build());
    }

}

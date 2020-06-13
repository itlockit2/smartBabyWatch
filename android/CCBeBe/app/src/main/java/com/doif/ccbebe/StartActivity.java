package com.doif.ccbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.doif.ccbebe.util.EasyLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartActivity extends AppCompatActivity {

    private String TAG = "[StartActSend] ";

    private EasyLoginButton kakaoLoginButton;

    private TextView email_sign_in_button;
    private TextView email_sign_up_button;

    private String serverUrl;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialization();

    }

    @Override
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
    }

    private void initialization(){

        // set serverUrl
        serverUrl = "https://ccbebe.io:3001";


        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        Log.d(TAG, "로그인 필요");

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("serverUrl", serverUrl);

        editor.commit();

        // bind view
        kakaoLoginButton = (EasyLoginButton)findViewById(R.id.kakaoLoginButton);
        email_sign_in_button = (TextView)findViewById(R.id.email_signin_button);
        email_sign_up_button = (TextView)findViewById(R.id.email_sign_up_button);

        // attach listener
        kakaoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        email_sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, PolicyActivity.class);
                startActivity(intent);
            }
        });
    }

    // hash 값 알아내기
    private void getHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.doif.ccbebe", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG,"key_hash="+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}

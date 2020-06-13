package com.doif.ccbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.util.DefaultEdittext;
import com.doif.ccbebe.util.UploadChooser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class SetBabyActivity extends AppCompatActivity {

    private String TAG = "[SetBbAct] ";

    private ImageView backFloatingButton_set_baby;
    private Button set_baby_button;

    private ImageView set_baby_photo;
    private ImageView set_baby_camera;

    private TextView set_baby_female;
    private TextView set_baby_male;

    private DefaultEdittext set_baby_nickname;
    private DefaultEdittext set_baby_birth;

    private UploadChooser uploadChooser;

    private String serverUrl;
    private String _id;
    private String userId;

    private String photo;
    private String nickname;
    private String sex;
    private String birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_baby);

        initialize();
    }

    private void initialize(){

        // initialize value
        photo = "";
        nickname = "";
        sex = "";
        birth = "";

        // bind view
        backFloatingButton_set_baby = findViewById(R.id.backFloatingButton_set_baby);
        set_baby_button = findViewById(R.id.set_baby_button);
        set_baby_photo = findViewById(R.id.set_baby_photo);
        set_baby_camera = findViewById(R.id.set_baby_camera);
        set_baby_nickname = findViewById(R.id.set_baby_nickname);
        set_baby_birth = findViewById(R.id.set_baby_birth);
        set_baby_female = findViewById(R.id.set_baby_female);
        set_baby_male = findViewById(R.id.set_baby_male);

        // get inform
        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        _id = sharedPreferences.getString("_id", null);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        userId = sharedPreferences.getString("userId", null);

        Log.d(TAG, "_id -> " + _id);

        // set image
        if(Build.VERSION.SDK_INT >= 21){
            set_baby_photo.setClipToOutline(true);
        }
        set_baby_photo.setBackground(new ShapeDrawable(new OvalShape()));

        // attach listener
        backFloatingButton_set_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        set_baby_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "추후 구현 예정입니다. " , Toast.LENGTH_SHORT).show();
//                uploadChooser = UploadChooser.getInstance(getApplicationContext(), SetBabyActivity.this, set_baby_photo);
//                uploadChooser.show(getSupportFragmentManager(), "");
            }
        });

        set_baby_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_baby_female.setBackgroundResource(R.drawable.bc_layout_green);
                set_baby_female.setTextColor(getResources().getColor(R.color.white));

                set_baby_male.setBackgroundResource(R.color.transparent);
                set_baby_male.setTextColor(getResources().getColor(R.color.grayDark));

                sex = "female";
            }
        });

        set_baby_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_baby_male.setBackgroundResource(R.drawable.bc_layout_green);
                set_baby_male.setTextColor(getResources().getColor(R.color.white));

                set_baby_female.setBackgroundResource(R.color.transparent);
                set_baby_female.setTextColor(getResources().getColor(R.color.grayDark));

                sex = "male";
            }
        });


        set_baby_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                photo = sharedPreferences.getString("babyPhoto", "empty");

                nickname = set_baby_nickname.getText();
                birth = set_baby_birth.getText();

                if(nickname.equals("")) nickname = "베베";
                if(sex.equals("")) sex = "female";
                if(birth.equals("")) birth = "20190101";

                new CreateBabyAsyncTask();
            }
        });
    }


    // create baby api
    private class CreateBabyAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[CrBabyAST] - ";


        private String createServerUrl;
        private String apiPath;

        private String errorResponse;

        public CreateBabyAsyncTask() {
            initialize();
            makeJson();
        }

        private void initialize() {
            apiPath = "/baby";
            createServerUrl = serverUrl + apiPath;

            Log.d(TAG, serverUrl);
        }

        private void makeJson() {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("parentId", userId);
                jsonObject.put("nickname", nickname);
                jsonObject.put("sex", sex);
                jsonObject.put("birth", birth);
                jsonObject.put("photo", photo);

                Log.d(TAG, photo);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject.length() > 0) {
                execute(String.valueOf(jsonObject));
            }
        }

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            BufferedWriter userRequest = null;
            BufferedReader serverResponse = null;

            int responseCode = 0;

            try {
                // set header
                url = new URL(createServerUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", _id);
                // send data to server
                userRequest = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                String body = params[0];
                Log.d(TAG, "<body> " + body);
                userRequest.write(body);
                userRequest.close();
                // response data from server
                responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, responseCode + "");
                serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = serverResponse.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                String result = stringBuffer.toString();
                Log.d(TAG, "<response> " + result);

                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();
                Log.e(TAG, "Server url is invalid", e);

                return "urlError";
            } catch (IOException e) {

                errorResponse = null;

                try {
                    serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;

                    while ((line = serverResponse.readLine()) != null) {
                        stringBuffer.append(line + "\n");
                    }

                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());

                    jsonObject = jsonObject.getJSONObject("error");
                    errorResponse = jsonObject.getString("message");

                    Log.d(TAG, "<error> " + errorResponse);

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    e.printStackTrace();
                    return "error";
                }

                e.printStackTrace();
                Log.d(TAG, "<error> " + errorResponse);
                Log.e(TAG, "IOException is occurred... ", e);

                StringTokenizer stringTokenizer = new StringTokenizer(e.getMessage());
                String errorMessage = stringTokenizer.nextToken();

                if (errorMessage.equals("failed"))
                    return "networkError";
                else
                    return "inputError";

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
                if (serverResponse != null) {
                    try {
                        serverResponse.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // 통신 오류
            if (result.equals("networkError")) {
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            // 입력 형태 오류
            else if (result.equals("inputError")) {
                Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getApplicationContext(), "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else {
                Toast.makeText(getApplicationContext(), "아기 프로필이 저장되었습니다.", Toast.LENGTH_SHORT);

                SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("babyName", nickname);
                editor.commit();

                Intent intent = new Intent(SetBabyActivity.this, SelectTypeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

}

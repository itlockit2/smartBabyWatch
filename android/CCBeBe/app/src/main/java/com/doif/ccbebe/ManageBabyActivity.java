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

import org.json.JSONArray;
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

import static com.doif.ccbebe.util.BitmapUtil.stringToBitmap;

public class ManageBabyActivity extends AppCompatActivity {

    private String TAG = "[mngBabyAct] - ";
    // view
    private ImageView backFloatingButton_manage_baby;
    private ImageView manage_baby_photo;
    private ImageView manage_baby_cameara;

    private DefaultEdittext manage_baby_nickname;
    private DefaultEdittext manage_baby_birth;

    private TextView manage_baby_female;
    private TextView manage_baby_male;

    private Button manage_baby_save_change_button;

    // photo
    private UploadChooser uploadChooser;

    private String _id;
    private String userId;
    private String serverUrl;

    private String nickname;
    private String sex;
    private String birth;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_baby);

        initialize();
        new GetBabyAsyncTask();
    }


    // 초기화
    private void initialize(){

        // initialize value
        nickname = "";
        sex = "";
        birth = "";
        photo = "";

        // bind view
        backFloatingButton_manage_baby = findViewById(R.id.backFloatingButton_manage_baby);
        manage_baby_photo = findViewById(R.id.manage_baby_photo);
        manage_baby_cameara = findViewById(R.id.manage_baby_camera);
        manage_baby_nickname = findViewById(R.id.manage_baby_nickname);
        manage_baby_birth = findViewById(R.id.manage_baby_birth);
        manage_baby_female = findViewById(R.id.manage_baby_female);
        manage_baby_male = findViewById(R.id.manage_baby_male);
        manage_baby_save_change_button = findViewById(R.id.manage_baby_save_change_button);

        // get information
        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        _id = sharedPreferences.getString("_id", null);
        userId = sharedPreferences.getString("userId", null);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");

        Log.d(TAG, "_id - " + _id + " / userId - " + userId );
        // set photo
        manage_baby_photo.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21){
            manage_baby_photo.setClipToOutline(true);
        }

        // attach listener
        backFloatingButton_manage_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        manage_baby_cameara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "추후 구현 예정입니다. " , Toast.LENGTH_SHORT).show();
//                uploadChooser = UploadChooser.getInstance(getApplicationContext(), ManageBabyActivity.this, manage_baby_photo);
//                uploadChooser.show(getSupportFragmentManager(), "");
            }
        });

        manage_baby_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manage_baby_female.setBackgroundResource(R.drawable.bc_layout_green);
                manage_baby_female.setTextColor(getResources().getColor(R.color.white));

                manage_baby_male.setBackgroundResource(R.color.transparent);
                manage_baby_male.setTextColor(getResources().getColor(R.color.grayDark));

                sex = "female";
            }
        });

        manage_baby_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manage_baby_male.setBackgroundResource(R.drawable.bc_layout_green);
                manage_baby_male.setTextColor(getResources().getColor(R.color.white));

                manage_baby_female.setBackgroundResource(R.color.transparent);
                manage_baby_female.setTextColor(getResources().getColor(R.color.grayDark));

                sex = "male";
            }
        });

        manage_baby_save_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new UpdateBabyAsyncTask();

            }
        });
    }

    // update baby api
    private class UpdateBabyAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[UdBabyAST] - ";


        private String updateServerUrl;
        private String apiPath;

        private String errorResponse;

        public UpdateBabyAsyncTask() {
            initialize();
            makeJson();
        }

        private void initialize() {
            apiPath = "/baby/" + userId;
            updateServerUrl = serverUrl + apiPath;

            SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            photo = sharedPreferences.getString("babyPhoto", "empty");

//            photo = "empty";

            nickname = manage_baby_nickname.getText();
            birth = manage_baby_birth.getText();

            if(nickname.equals("")) nickname = "베베";
            if(birth.equals("")) birth = "20190101";
            if(sex.equals("")) sex = "female";

        }

        private void makeJson() {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("parentId", userId);
                jsonObject.put("nickname", nickname);
                jsonObject.put("sex", sex);
                jsonObject.put("birth", birth);
                jsonObject.put("photo", photo);


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
                url = new URL(updateServerUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", _id);

                Log.d(TAG, "_id - " + _id);
                // send data to server
                userRequest = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
                String body = params[0];
                Log.d(TAG, "<body> " + body);
                userRequest.write(body);
                userRequest.close();
                // response data from server
                responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "responseCode - " + responseCode);
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
                Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else {
                Toast.makeText(getApplicationContext(), "아기 프로필이 변경되었습니다.", Toast.LENGTH_SHORT);

                SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("babyName", nickname);
                editor.commit();

                Intent intent = new Intent(ManageBabyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    // get baby api
    private class GetBabyAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[GetBabyAT]";

        private String apiPath;
        private String getServerUrl;

        private String errorResponse;

        public GetBabyAsyncTask(){

            initialize();
            execute();

        }

        private void initialize(){
            apiPath = "/baby/";

            apiPath = apiPath + userId;
            getServerUrl = serverUrl + apiPath;

            Log.d(TAG, _id);
            Log.d(TAG, serverUrl);
        }

        @Override
        protected String doInBackground(String... params){

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            BufferedReader serverResponse = null;
            int responseCode = 0;

            try{
                // set header
                url = new URL(getServerUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Authorization", _id);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                // response data from server
                responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "<response code> " + responseCode);

                serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while((line = serverResponse.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                String result = stringBuffer.toString();

                Log.d(TAG, "<response>" + result);

                return result;

            }catch (MalformedURLException e){

                e.printStackTrace();
                Log.e(TAG, "Server url is invalid", e);

                return "urlError";
            }catch (IOException e){

                errorResponse = null;

                try{
                    serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;

                    while((line = serverResponse.readLine()) != null){
                        stringBuffer.append(line + "\n");
                    }

                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());

                    jsonObject = jsonObject.getJSONObject("error");
                    errorResponse = jsonObject.getString("message");

                    Log.d(TAG, "<error> " + errorResponse);

                }catch (IOException ex){
                    ex.printStackTrace();
                }catch (JSONException ex){
                    e.printStackTrace();
                }catch (Exception ex){
                    e.printStackTrace();
                    return "error";
                }

                e.printStackTrace();
                Log.e(TAG, "IOException is occurred... ", e);

                StringTokenizer stringTokenizer = new StringTokenizer(e.getMessage());
                String errorMessage = stringTokenizer.nextToken();

                if(errorMessage.equals("failed"))
                    return "networkError";
                else
                    return "inputError";

            }catch(Exception e){
                e.printStackTrace();
                return "error";
            }finally {
                if(httpURLConnection != null) httpURLConnection.disconnect();
                if(serverResponse != null){
                    try{
                        serverResponse.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected  void onPostExecute(String result){
            super.onPostExecute(result);
            // 통신 오류
            if(result.equals("networkError")){
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            // 입력 형태 오류
            else if(result.equals("inputError")){
                    Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();
            }else if(result.equals("error")){
                Toast.makeText(getApplicationContext(), "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                try{

                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    // set photo
                    photo = jsonObject.getString("photo");

                    manage_baby_photo.setBackground(new ShapeDrawable(new OvalShape()));
                    if(Build.VERSION.SDK_INT >= 21){
                        manage_baby_photo.setClipToOutline(true);
                    }

                    if(photo.equals("empty")){
                        manage_baby_photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                    }else{
                        manage_baby_photo.setImageBitmap(stringToBitmap(photo));
                        manage_baby_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    // set nickname
                    nickname = jsonObject.getString("nickname");
                    manage_baby_nickname.setText(nickname);
                    // set sex
                    sex = jsonObject.getString("sex");
                    if(sex.equals("female")){
                        manage_baby_female.setBackgroundResource(R.drawable.bc_layout_green);
                        manage_baby_female.setTextColor(getResources().getColor(R.color.white));

                        manage_baby_male.setBackgroundResource(R.color.transparent);
                        manage_baby_male.setTextColor(getResources().getColor(R.color.grayDark));
                    }else{
                        manage_baby_male.setBackgroundResource(R.drawable.bc_layout_green);
                        manage_baby_male.setTextColor(getResources().getColor(R.color.white));

                        manage_baby_female.setBackgroundResource(R.color.transparent);
                        manage_baby_female.setTextColor(getResources().getColor(R.color.grayDark));
                    }
                    // set birth
                    birth = jsonObject.getString("birth");
                    manage_baby_birth.setText(birth);

                }catch (JSONException e){
                    Log.d(TAG, "json exception is occurs");
                }
            }
        }
    }

}

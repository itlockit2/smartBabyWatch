package com.doif.ccbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActSend :  ";

    private String userId;
    private String serverUrl;
    private String _id;

    private String eventJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        _id = sharedPreferences.getString("_id", null);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");

        if(userId != null){
            new BabyAsyncTask();
        }else{
            startLoading(2000);
        }

    }

    private void startLoading(int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        }, delay);
    }

    // get event api
    private class EventAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[EventAT]";

        private String eventUrl;
        private String apiPath;

        private String errorResponse;

        public EventAsyncTask(){

            Log.d(TAG, "Event Asyn Task is started");
            initialize();
            execute();

        }

        private void initialize(){
            apiPath = "/event/";

            apiPath = apiPath + userId;
            eventUrl = serverUrl + apiPath;

        }

        @Override
        protected String doInBackground(String... params){

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            BufferedReader serverResponse = null;
            int responseCode = 0;

            try{
                // set header
                url = new URL(eventUrl);
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

                Log.d(TAG, "errorMessage - " + errorMessage);

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
                if(errorResponse.equals(null))
                    Toast.makeText(getApplicationContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                else {
                    if(!errorResponse.substring(0, 2).equals("해당"))
                        Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();

                    eventJson = "";
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("event_json", eventJson);
                    startActivity(intent);
                    finish();
                }
            }else if(result.equals("error")){
                Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                eventJson = result;

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("event_json", eventJson);
                startActivity(intent);
                finish();

            }
        }
    }

    // get event api
    private class BabyAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[BabyAT]";

        private String babyUrl;
        private String apiPath;

        private String errorResponse;

        public BabyAsyncTask(){

            initialize();
            execute();

        }

        private void initialize(){
            apiPath = "/baby/";

            apiPath = apiPath + userId;
            babyUrl = serverUrl + apiPath;

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
                url = new URL(babyUrl);
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

                Log.d(TAG, "errorMessage - " + errorMessage);

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
                if(errorResponse.equals(null))
                    Toast.makeText(getApplicationContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                else {
                    if(!errorResponse.substring(0, 2).equals("해당"))
                        Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();

                    new EventAsyncTask();
                }
            }else if(result.equals("error")){
                Toast.makeText(getApplicationContext(), "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{
                Log.d(TAG, "finish");
                new EventAsyncTask();
            }
        }
    }
}

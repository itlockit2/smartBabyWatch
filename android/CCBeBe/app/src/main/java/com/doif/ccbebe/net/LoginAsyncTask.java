package com.doif.ccbebe.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.SelectTypeActivity;
import com.doif.ccbebe.SetBabyActivity;

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

public class LoginAsyncTask extends AsyncTask<String, Void, String>  {

    private String TAG = "[LoginAT] - ";

    private Context context;
    private Activity activity;

    private String email;
    private String password;
    private String appToken;

    private String serverUrl;
    private String apiPath;
    private String loginServerUrl;

    private String _id;
    private String userId;

    private String errorResponse;


    public LoginAsyncTask(Context context, Activity activity, String email, String password){
        this.context = context;
        this.activity = activity;
        this.email = email;
        this.password = password;

        initialize();
        makeJson();
    }

    private void initialize(){
        apiPath = "/user/login";

        SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        appToken = sharedPreferences.getString("appToken", null);

        Log.d(TAG, "appToken - " + appToken);
        loginServerUrl = serverUrl + apiPath;

        Log.d(TAG, serverUrl);
    }

    private void makeJson(){

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("appToken", appToken);

        }catch (JSONException e){
            e.printStackTrace();
        }

        if(jsonObject.length() > 0 ){
            execute(String.valueOf(jsonObject));
        }
    }

    @Override
    protected String doInBackground(String... params){

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedWriter userRequest = null;
        BufferedReader serverResponse = null;
        int responseCode = 0;

        try{
            // set header
            url = new URL(loginServerUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // send data to server
            userRequest = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            String body = params[0];
            Log.d(TAG, "<body> " + body);
            userRequest.write(body);
            userRequest.close();
            // response data from server
            responseCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseCode);
            serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while((line = serverResponse.readLine()) != null){
                stringBuffer.append(line + "\n");
            }
            String result = stringBuffer.toString();
            Log.d(TAG, "<response> " + result);

            return result;

        }catch(MalformedURLException e){

            e.printStackTrace();
            Log.e(TAG, "Server url is invalid.", e);

            return "urlError";

        }catch(IOException e){

            errorResponse = null;

            try{
                serverResponse = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;

                while((line = serverResponse.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                JSONObject errorJson = new JSONObject(stringBuffer.toString());

                errorJson = errorJson.getJSONObject("error");
                errorResponse = errorJson.getString("message");

                Log.d(TAG, "<error> " + errorResponse);

            }catch(IOException ex){
                ex.printStackTrace();
            }catch(JSONException ex){
                ex.printStackTrace();
            }

            e.printStackTrace();
            Log.e(TAG, "IOException is occured...",e);

            StringTokenizer errorType = new StringTokenizer(e.getMessage());
            String errorMessage = errorType.nextToken();

            Log.d(TAG, "errorMessage - " + errorMessage);

            if(errorMessage.equals("failed"))
                return "networkError";
            else
                return "inputError";


        }finally {
            if(httpURLConnection != null) httpURLConnection.disconnect();
            if(serverResponse != null){
                try{
                    serverResponse.close();
                }catch(IOException e){
                    e.printStackTrace();
                    Log.d(TAG, "IOException is occured...");
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        // 통신 오류
        if(result.equals("networkError")){
            Toast.makeText(context, "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
        // 입력 형태 오류
        else if(result.equals("inputError")){
            if(errorResponse.length() == 0)
                Toast.makeText(context, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, errorResponse, Toast.LENGTH_SHORT).show();
        }
        // 정상인 경우
        else{
            try{
                JSONObject jsonObject = new JSONObject(result);
                userId = jsonObject.getString("userId");
                _id = jsonObject.getString("_id");

                Log.d(TAG, "userId : " + userId + " , _id : " + _id);

                SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("userId", userId);
                editor.putString("_id", _id);

                editor.commit();

                Toast.makeText(context, "환영합니다.", Toast.LENGTH_SHORT).show();

                new GetBabyAsyncTask();

            }catch (JSONException e){
                e.printStackTrace();
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
                Toast.makeText(context, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
            // 입력 형태 오류
            else if(result.equals("inputError")){
                if(errorResponse.length() == 0)
                    Toast.makeText(context, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                else{
//                    Toast.makeText(context, errorResponse, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, SetBabyActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                }
            }else if(result.equals("error")){
                Toast.makeText(context, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                try{
                    JSONObject jsonObject = (new JSONArray(result)).getJSONObject(0);
                    String babyPhoto = jsonObject.getString("photo");

                    SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("babyPhoto", null);
                    editor.commit();

                    Intent intent = new Intent(context, SelectTypeActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }catch (JSONException e){
                    Log.d(TAG, "json exception is occurs");
                }

            }
        }
    }
}

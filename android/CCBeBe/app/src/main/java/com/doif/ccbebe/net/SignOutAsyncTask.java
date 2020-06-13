package com.doif.ccbebe.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class SignOutAsyncTask extends AsyncTask<String, Void, String>  {

    private String TAG = "[SignOutAT] - ";

    private Context context;
    private Activity activity;

    private String serverUrl;
    private String apiPath;
    private String _id;
    private String userId;
    private String appToken;

    private String errorResponse;

    public SignOutAsyncTask(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        initialize();
        execute();
    }

    private void initialize(){

        apiPath = "/token/";

        SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        _id = sharedPreferences.getString("_id", null);

        apiPath = apiPath + _id;
        serverUrl = serverUrl + apiPath;

        Log.d(TAG, serverUrl);
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
            url = new URL(serverUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Authorization", _id);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // response data from server
            responseCode = httpURLConnection.getResponseCode();
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

            SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            appToken = sharedPreferences.getString("appToken", null);
            serverUrl = sharedPreferences.getString("serverUrl", null);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();

            editor.putString("appToken", appToken);
            editor.putString("serverUrl", serverUrl);

            editor.commit();

            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, StartActivity.class);
            activity.startActivity(intent);
            activity.finish();

        }
    }


}

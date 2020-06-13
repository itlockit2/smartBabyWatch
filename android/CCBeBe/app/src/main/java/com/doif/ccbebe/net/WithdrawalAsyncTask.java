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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WithdrawalAsyncTask extends AsyncTask<String, Void, String>  {

    private String TAG = "[withdrawAST] - ";

    private Context context;
    private Activity activity;

    private String userId;
    private String _id;
    private String appToken;
    private String serverUrl;
    private String apiPath;


    public WithdrawalAsyncTask(Context context, Activity activity, String userId, String _id){
        this.context = context;
        this.activity = activity;
        this.userId = userId;
        this._id = _id;

        initialize();
        execute();
    }

    public WithdrawalAsyncTask(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        initialize();
        execute();
    }

    private void initialize(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);

        appToken = sharedPreferences.getString("appToken", null);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        userId = sharedPreferences.getString("userId", null);
        _id = sharedPreferences.getString("_id", null);

        apiPath = "/user/" + userId;
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
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", _id);
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
            e.printStackTrace();
            Log.e(TAG, "IOException is occured...",e);

            if(responseCode == 500)
                return "inputError";
            else
                return "networkError";

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
            Toast.makeText(context, "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
        }
        // 정상인 경우
        else{

            SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();

            editor.putString("appToken", appToken);
            editor.putString("serverUrl", serverUrl);

            editor.commit();

            Intent intent = new Intent(activity, StartActivity.class);
            activity.startActivity(intent);
            activity.finish();

            Toast.makeText(context, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();

        }
    }


}

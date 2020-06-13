package com.doif.ccbebe.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.SuccessSignUpActivity;

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

public class CreateUserAsyncTask extends AsyncTask<String, Void, String>  {

    private String TAG = "[CreateUserAT] - ";

    private Context context;
    private Activity activity;

    private String email;
    private String password;
    private String errorResponse;

    private String serverUrl;
    private String apiPath;


    public CreateUserAsyncTask(Context context, Activity activity, String email, String password){
        this.context = context;
        this.activity = activity;
        this.email = email;
        this.password = password;

        initialize();
        makeJson();
    }

    private void initialize(){
        apiPath = "/user";

        SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
        serverUrl = serverUrl + apiPath;

        errorResponse = null;
    }

    private void makeJson(){

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("email", email);
            jsonObject.put("password", password);
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
            url = new URL(serverUrl);
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
                String user_id = jsonObject.getString("_id");

                Toast.makeText(context, "회원 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, SuccessSignUpActivity.class);
//                Intent intent = new Intent(context, SetBabyActivity.class);
                activity.startActivity(intent);
                activity.finish();

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


}

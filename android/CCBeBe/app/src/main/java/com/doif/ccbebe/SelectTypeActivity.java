package com.doif.ccbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class SelectTypeActivity extends AppCompatActivity {

    private String TAG = "[SelecTypeAct] ";

    private ImageView backFloatingButton_select_type;
    private ConstraintLayout select_type_monitoring_layout;
    private ConstraintLayout select_type_shooting_layout;
    private ImageView checked_monitoring_image;
    private ImageView checked_shooting_image;
    private Button select_type_start_button;

    private String isStreamer;
    private String eventJson;

    private String serverUrl;
    private String userId;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        initialize();
    }

    private void initialize(){

        // initialize
        isStreamer = "false";

        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        _id = sharedPreferences.getString("_id", null);
        serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");

        // bind view
        backFloatingButton_select_type = findViewById(R.id.backFloatingButton_select_type);
        select_type_monitoring_layout = findViewById(R.id.select_type_monitoring_layout);
        select_type_shooting_layout = findViewById(R.id.select_type_shooting_layout);
        checked_monitoring_image = findViewById(R.id.checked_monitoring_image);
        checked_shooting_image = findViewById(R.id.checked_shooting_image);
        select_type_start_button = findViewById(R.id.select_type_start_button);

        // set button background
        select_type_monitoring_layout.setBackgroundResource(R.drawable.bc_edit_text);
        select_type_shooting_layout.setBackgroundResource(R.drawable.bc_edit_text);

        checked_monitoring_image.setVisibility(View.GONE);
        checked_shooting_image.setVisibility(View.GONE);

        // attach listener
        backFloatingButton_select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_type_monitoring_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_type_monitoring_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);
                select_type_shooting_layout.setBackgroundResource(R.drawable.bc_edit_text);

                checked_monitoring_image.setVisibility(View.VISIBLE);
                checked_shooting_image.setVisibility(View.GONE);

                isStreamer = "false";

            }
        });

        select_type_shooting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_type_monitoring_layout.setBackgroundResource(R.drawable.bc_edit_text);
                select_type_shooting_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);

                checked_monitoring_image.setVisibility(View.GONE);
                checked_shooting_image.setVisibility(View.VISIBLE);

                isStreamer = "true";
            }
        });

        select_type_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new EventAsyncTask();
            }
        });

    }


    // get event api
    private class EventAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[EventAT]";

        private String eventUrl;
        private String apiPath;

        private String errorResponse;

        public EventAsyncTask(){
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

                    SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("isStreamer", isStreamer);
                    editor.commit();

                    Intent intent = new Intent(SelectTypeActivity.this, MainActivity.class);
                    intent.putExtra("event_json", eventJson);
                    startActivity(intent);
                    finish();
                }
            }else if(result.equals("error")){
                Toast.makeText(getApplicationContext(), "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                eventJson = result;

                SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("isStreamer", isStreamer);
                editor.commit();

                Intent intent = new Intent(SelectTypeActivity.this, MainActivity.class);
                intent.putExtra("event_json", eventJson);
                startActivity(intent);
                finish();

            }
        }
    }
}

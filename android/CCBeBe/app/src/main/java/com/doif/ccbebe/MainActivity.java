package com.doif.ccbebe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.util.MenuDialog;
import com.doif.ccbebe.util.MainRecyclerviewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    private String TAG = "[MainActSend] ";

    // floating menu
    private ImageView menuFloatingButton;
    private FloatingActionButton cameraFloatingButton;

    private RecyclerView main_recyclerview;

    private DisplayMetrics metrics;

    private String eventJson;

    private String _id;
    private String userId;
    private String isStreamer;
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }

    @Override
    protected void onResume(){
        super.onResume();

        new EventAsyncTask();
    }

    @Override
    public void onBackPressed(){
        ActivityCompat.finishAffinity(this);
    }

    private void initialization() {

        try{
            // set data
            SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");
            _id = sharedPreferences.getString("_id", null);
            userId = sharedPreferences.getString("userId", null);
            isStreamer = sharedPreferences.getString("isStreamer", "false");

            // get display size
            metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            // bind view
            menuFloatingButton = (ImageView) findViewById(R.id.menuFloatingButton);
            cameraFloatingButton = (FloatingActionButton) findViewById(R.id.cameraFloatingButton);

            // set settign menu button
            menuFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    WindowManager windowManager = (WindowManager) getApplicationContext()
                            .getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                    new MenuDialog(MainActivity.this, MainActivity.this, displayMetrics).show();

                }
            });

            // set cameara button
            cameraFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CallActivity.class);
                    startActivity(intent);
                }
            });

            // get intent  data - event json
            Intent intent = getIntent();
            eventJson = intent.getStringExtra("event_json");

            Log.d(TAG, "event -> " + eventJson);

            if(eventJson != null)
                setEventJsonArray(new JSONArray(eventJson), isStreamer);

        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "json exception is occures", e);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "서버 에러 입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // set timeline
    private void setEventJsonArray(JSONArray jsonArray, String isStreamer) {

        main_recyclerview = (RecyclerView) findViewById(R.id.main_recyclerview);

        main_recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        main_recyclerview.setAdapter(new MainRecyclerviewAdapter(MainActivity.this, metrics, jsonArray, isStreamer));
    }

    // get event api
    private class EventAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[EventAT]";

        private String apiPath;

        private String errorResponse;

        public EventAsyncTask(){

            initialize();
            execute();

        }

        private void initialize(){
            apiPath = "/event/";

            SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            serverUrl = sharedPreferences.getString("serverUrl", "https://ccbebe.io:3001");

            apiPath = apiPath + userId;
            serverUrl = serverUrl + apiPath;

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
                url = new URL(serverUrl);
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
                if(errorResponse.equals(null))
                    Toast.makeText(getApplicationContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                else{
                    if(!errorResponse.substring(0, 2).equals("해당"))
                        Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();
                    setEventJsonArray(new JSONArray(), isStreamer);
                }
            }else if(result.equals("error")){
                Toast.makeText(getApplicationContext(), "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                try{

                    setEventJsonArray(new JSONArray(result), isStreamer);

                }catch (JSONException e){

                }
            }
        }
    }

}

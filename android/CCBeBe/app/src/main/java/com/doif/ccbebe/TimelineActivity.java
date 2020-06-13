package com.doif.ccbebe;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.util.TimelineRecyclerviewAdapter;

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

public class TimelineActivity extends AppCompatActivity {

    private ImageView backFloatingButton_timeline;
    private RecyclerView timeline_recyclerview;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        initialize();
        new EventAsyncTask();
    }

    private void initialize(){

        // set display size
        metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        // bind view
        backFloatingButton_timeline = findViewById(R.id.backFloatingButton_timeline);
        timeline_recyclerview = findViewById(R.id.timeline_recyclerview);

        // attach listner
        backFloatingButton_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    // set timeline
    private void setEventJsonArray(JSONArray jsonArray) {

        timeline_recyclerview.setLayoutManager(new LinearLayoutManager(TimelineActivity.this));
        timeline_recyclerview.setAdapter(new TimelineRecyclerviewAdapter(TimelineActivity.this, metrics, jsonArray));
    }

    // get event api
    private class EventAsyncTask extends AsyncTask<String, Void, String> {

        private String TAG = "[EventAT]";

        private String _id;
        private String userId;
        private String isStreamer;

        private String serverUrl;
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
            _id = sharedPreferences.getString("_id", null);
            userId = sharedPreferences.getString("userId", null);
            isStreamer = sharedPreferences.getString("isStreamer", "false");

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
                else
                    Toast.makeText(getApplicationContext(), errorResponse, Toast.LENGTH_SHORT).show();
            }
            // 정상인 경우
            else{

                try{

                    setEventJsonArray(new JSONArray(result));

                }catch (JSONException e){

                }
            }
        }
    }

}

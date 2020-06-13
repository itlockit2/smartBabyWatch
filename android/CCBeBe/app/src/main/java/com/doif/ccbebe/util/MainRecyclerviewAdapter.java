package com.doif.ccbebe.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainRecyclerviewAdapter extends RecyclerView.Adapter {

    private String TAG = "[TLRecyclerAdapter] ";

    private Context context;

    private JSONArray contents;

    private String userId;
    private String eventUrl;
    private String fileName;
    private String isStreamer;
    private String status;

    private DisplayMetrics metrics;

    private boolean isEmpty;

    public MainRecyclerviewAdapter(Context context, DisplayMetrics metrics, JSONArray contents, String isStreamer){

        this.context = context;
        this.metrics = metrics;
        this.contents = contents;
        this.isStreamer = isStreamer;

        Log.d(TAG, "isStreamer = " + isStreamer);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timeline, parent, false);

        return new MessageVieHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try{

            MessageVieHolder messageVieHolder = ((MessageVieHolder) holder);

            // if event database is empty
            if(isEmpty) messageVieHolder.timeline_button.setVisibility(View.GONE);

            // set balloon
            SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            String babyPhoto = sharedPreferences.getString("babyPhoto", "empty");
            String babyName = sharedPreferences.getString("babyName", "베베");
            isStreamer = sharedPreferences.getString("isStreamer", "false");

            if(babyPhoto != "empty"){
                messageVieHolder.main_balloon.setPhoto(babyPhoto);
                messageVieHolder.main_balloon.setScaleCenter();
            }

            messageVieHolder.main_balloon.setHelloString(babyName);

            if(isStreamer.equals("true")){
                Log.d(TAG, "this is true");
                messageVieHolder.main_balloon.setHelloString("촬영 스마트폰 입니다", true);
            }

            // set event
            JSONObject jsonObject = contents.getJSONObject(position);

            if(position == 0){
                messageVieHolder.main_balloon.setVisibility(View.VISIBLE);
            }else{
                messageVieHolder.main_balloon.setVisibility(View.GONE);
            }

            // set timeline
            messageVieHolder.timeline_button.setTimeString(jsonObject.getString("createdDate"));

            Log.d(TAG, position + " - " + jsonObject.getString("createdDate") + " - " + jsonObject.getString("status"));



            switch (jsonObject.getString("status")) {
                case "update":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.update));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.update));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_update);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_main);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "shoot":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.shoot));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.shoot));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_shoot);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.grayDark));
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_main);
                    break;
                case "awake":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.awake));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.awake));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_awake);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_awake);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "sleep":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.sleep));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.sleep));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_sleep);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_sleep);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "cry":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.cry));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.cry));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_cry);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_cry);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "notFace":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_danger);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_danger);
//                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.danger_balloon));
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "Angry":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.anger));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.anger));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_angry);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_angry);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "Disgust":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.disgust));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.disgust));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_disgust);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_disgust);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "Fear":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.fear));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.fear));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_fear);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_fear);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                case "Surprise":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.surprise));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.surprise));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_surprise);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_surprise);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.grayDark));
                    break;
                case "Neutral":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.neutral));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.neutral));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_neutral);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_neutral);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.grayDark));
                    break;
                case "Happy":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.happy));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.happy));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_happy);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_happy);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.grayDark));
                    break;
                case "Sad":
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.sad));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.sad));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_sad);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_sad);
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
                default :
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setStateString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setContainer(R.drawable.bc_timeline_danger);
                    messageVieHolder.main_balloon.setContainer(R.drawable.balloon_danger);
                    messageVieHolder.main_balloon.setBirthString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_button.setTimeColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setHelloColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.main_balloon.setBirthColor(context.getResources().getColor(R.color.white));
                    break;
            }


            userId = jsonObject.getString("userId");
            fileName = jsonObject.getString("fileName");
            status = jsonObject.getString("status");
            eventUrl = "https://ccbebe.io/video/" + userId + "-camera/" + status + "/" + fileName;

            Log.d(TAG, eventUrl);

            messageVieHolder.timeline_button.setOnClickListener(new EventOnClickListener(eventUrl){
                @Override
                public void onClick(View view) {
                    Log.d(TAG, messageVieHolder.timeline_button.getTimeString() +"clicked");
                    Log.d(TAG, "eventurl - " +  event_url);
                    new EventDialog(context, metrics, messageVieHolder.timeline_button.getTimeString(), messageVieHolder.timeline_button.getStateString(), event_url).show();
                }
            });

        }catch(JSONException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, "서버 오류 입니다.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount(){

        isEmpty = contents.length() == 0 ? true : false;

        if(isEmpty) return 1;
        else return contents.length();
    }


    class MessageVieHolder extends RecyclerView.ViewHolder {

        public MainBalloon main_balloon;
        public TimelineButton timeline_button;

        public MessageVieHolder(View view){
            super(view);

            main_balloon = view.findViewById(R.id.main_balloon);
            timeline_button = view.findViewById(R.id.timeline_button);
        }
    }

    public class EventOnClickListener implements View.OnClickListener {

        String event_url;

        public EventOnClickListener(String e){
            event_url = e;
        }

        @Override
        public void onClick(View args){

        }

    }
}

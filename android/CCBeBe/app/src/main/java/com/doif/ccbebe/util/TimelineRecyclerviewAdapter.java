package com.doif.ccbebe.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimelineRecyclerviewAdapter extends RecyclerView.Adapter {

    private String TAG = "[TLRecyclerAdapter] ";

    private Context context;

    private JSONArray contents;

    private String userId;
    private String eventUrl;

    private DisplayMetrics metrics;

    public TimelineRecyclerviewAdapter(Context context, DisplayMetrics metrics){

        this.context = context;
        this.metrics = metrics;

        contents = new JSONArray();

    }

    public TimelineRecyclerviewAdapter(Context context, DisplayMetrics metrics, JSONArray contents){

        this.context = context;
        this.metrics = metrics;
        this.contents = contents;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_entire_timeline, parent, false);

        return new MessageVieHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try{

            MessageVieHolder messageVieHolder = ((MessageVieHolder) holder);
            JSONObject jsonObject = contents.getJSONObject(position);

            // set timeline
            messageVieHolder.timeline_entire_button.setTimeString(jsonObject.getString("createdDate"));

            Log.d(TAG, position + " - " + jsonObject.getString("createdDate") + " - " + jsonObject.getString("status"));

            switch (jsonObject.getString("status")) {
                case "update":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.update));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_update);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "shoot":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.shoot));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_shoot);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    break;
                case "awake":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.awake));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_awake);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "sleep":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.sleep));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_sleep);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "cry":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.cry));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_cry);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "notFace":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.danger));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_danger);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "Angry":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.anger));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_angry);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "Disgust":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.disgust));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_disgust);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "Fear":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.fear));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_fear);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                case "Surprise":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.surprise));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_surprise);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    break;
                case "Neutral":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.neutral));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_neutral);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    break;
                case "Happy":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.happy));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_happy);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.blackLight));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.blackLight));
                    break;
                case "Sad":
                    messageVieHolder.timeline_entire_button.setStateString(context.getResources().getString(R.string.sad));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_sad);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
                default :
                    messageVieHolder.timeline_entire_button.setStateString(jsonObject.getString("contents"));
                    messageVieHolder.timeline_entire_button.setContainer(R.drawable.bc_timeline_danger);
                    messageVieHolder.timeline_entire_button.setStateColor(context.getResources().getColor(R.color.white));
                    messageVieHolder.timeline_entire_button.setTimeColor(context.getResources().getColor(R.color.white));
                    break;
            }

            userId = jsonObject.getString("userId");
            eventUrl = jsonObject.getString("fileName");
            eventUrl = "https://ccbebe.io/video/" + userId + "/" + eventUrl;
//            eventUrl = "https://ccbebe.io/video/test.webm";

            Log.d(TAG, eventUrl);

            messageVieHolder.timeline_entire_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new EventDialog(context, metrics, messageVieHolder.timeline_entire_button.getTimeString(), messageVieHolder.timeline_entire_button.getStateString(), eventUrl).show();
                }
            });

        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount(){
        return contents.length();
    }


    class MessageVieHolder extends RecyclerView.ViewHolder {

        public TimelineButton timeline_entire_button;

        public MessageVieHolder(View view){
            super(view);

            timeline_entire_button = view.findViewById(R.id.timeline_entire_button);
        }
    }

}

package com.doif.ccbebe.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.R;

public class DiaryRecyclerviewAdapter extends RecyclerView.Adapter {

    private String TAG = "[DiaryRccAdapter] ";

    private Context context;

    private String contents[][];

    private String diaryUrl;

    private DisplayMetrics metrics;

    public DiaryRecyclerviewAdapter(Context context, DisplayMetrics metrics){

        this.context = context;
        this.metrics = metrics;

        contents = new String[6][4];

        contents[0][0] = "05:00"; contents[0][1] = "오늘"; contents[0][2] = "베베 100일째 되는 날";
        contents[1][0] = "05:00"; contents[1][1] = "1일 전"; contents[1][2] = "베베 99일째 되는 날";
        contents[2][0] = "05:00"; contents[2][1] = "2일 전"; contents[2][2] = "베베 98일째 되는 날";
        contents[3][0] = "05:00"; contents[3][1] = "3일 전"; contents[3][2] = "베베 97일째 되는 날";
        contents[4][0] = "05:00"; contents[4][1] = "4일 전"; contents[4][2] = "베베 96일째 되는 날";
        contents[5][0] = "05:00"; contents[5][1] = "5일 전"; contents[5][2] = "베베 95일째 되는 날";

//        diaryUrl = "https://ccbebe.io/video/test" + ".png";

        contents[0][3] = "https://cdn.pixabay.com/photo/2017/08/08/03/50/baby-2610206_1280.jpg";
        contents[1][3] = "https://cdn.pixabay.com/photo/2017/08/08/03/50/baby-2610204_1280.jpg";
        contents[2][3] = "https://cdn.pixabay.com/photo/2017/11/12/16/18/people-2942980_1280.jpg";
        contents[3][3] = "https://cdn.pixabay.com/photo/2014/04/02/08/42/baby-303068_1280.jpg";
        contents[4][3] = "https://cdn.pixabay.com/photo/2014/04/02/08/42/baby-303068_1280.jpg";
        contents[5][3] = "https://cdn.pixabay.com/photo/2014/04/02/08/42/baby-303068_1280.jpg";

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_diary, parent, false);

        return new MessageVieHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageVieHolder messageVieHolder = ((MessageVieHolder) holder);

        // set title size
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageVieHolder.diary_title.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels / 2;
        messageVieHolder.diary_title.setLayoutParams(params);

        // set title visible
        if(position == 0){
            messageVieHolder.diary_title.setVisibility(View.VISIBLE);
        }else{
            messageVieHolder.diary_title.setVisibility(View.GONE);
        }

        // set thumbnail image
        diaryUrl = contents[position][3];
        messageVieHolder.diary_thumbnail.setDiaryThumnail(diaryUrl);

        // set thumbnail description
        messageVieHolder.diary_thumbnail.setTimeString(contents[position][0]);
        messageVieHolder.diary_thumbnail.setDateString(contents[position][1]);
        messageVieHolder.diary_thumbnail.setDDayString(contents[position][2]);

        Log.d(TAG, position + " - " + contents[position][0] + " - " + contents[position][1]);

    }

    @Override
    public int getItemCount(){
        return contents.length;
    }


    class MessageVieHolder extends RecyclerView.ViewHolder {

        public LinearLayout diary_title;
        public DiaryThumbnailLayout diary_thumbnail;

        public MessageVieHolder(View view){
            super(view);

            diary_title = view.findViewById(R.id.diary_title);
            diary_thumbnail = view.findViewById(R.id.diary_thumbnail);
        }
    }

}

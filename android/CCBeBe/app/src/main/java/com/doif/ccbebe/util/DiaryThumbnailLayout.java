package com.doif.ccbebe.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.doif.ccbebe.DiaryVideoActivity;
import com.doif.ccbebe.R;

public class DiaryThumbnailLayout extends ConstraintLayout {

    private String TAG = "[DiaryThumbLo] ";

    Context context;

    ImageView diary_thumbnail_image;
    TextView diary_time;
    ImageView diary_play_button;
    TextView diary_date;
    TextView diary_see_more_button;
    TextView diary_d_day;

    public DiaryThumbnailLayout(Context context){
        super(context);

        this.context = context;
        initialization();
    }

    public DiaryThumbnailLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        this.context = context;
        initialization();
        getAttributeSet(attributeSet);
    }

    public DiaryThumbnailLayout(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        this.context = context;
        initialization();
        getAttributeSet(attributeSet, defStyle);
    }

    private void initialization(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.layout_diary_thumbnail, this, false);
        addView(v);

        diary_thumbnail_image = findViewById(R.id.diary_thumbnail_image);
        diary_time = findViewById(R.id.diary_time);
        diary_play_button = findViewById(R.id.diary_play_button);
        diary_date = findViewById(R.id.diary_date);
        diary_see_more_button = findViewById(R.id.diary_see_more_button);
        diary_d_day = findViewById(R.id.diary_d_day);
    }

    private void getAttributeSet(AttributeSet attributeSet){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.custom);
        setTypeArray(typedArray);
    }

    private void getAttributeSet(AttributeSet attributeSet, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.custom, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray){

        String time_string = typedArray.getString(R.styleable.custom_time);
        diary_time.setText(time_string);

        String date_string = typedArray.getString(R.styleable.custom_date);
        diary_date.setText(date_string);

        String description_string = typedArray.getString(R.styleable.custom_description);
        diary_d_day.setText(description_string);

        typedArray.recycle();

    }

    void setDiaryThumnail(String imagePath){
        Uri uri = Uri.parse(imagePath);
        Glide.with(context).load(uri).centerCrop().into(diary_thumbnail_image);
    }

    void setPlayButtonClickListener(String url, Context context){

        diary_play_button.setOnClickListener(new PlayButtonOnClickListener(url, context){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(thisContext, DiaryVideoActivity.class);
                intent.putExtra("videoUrl", videoUrl);
                context.startActivity(intent);
            }
        });
    }

    void setTimeString(String time_string){
        diary_time.setText(time_string);
    }

    String getTimeString(){
        return diary_time.getText().toString();
    }

    void setDateString(String state_string){
        diary_date.setText(state_string);
    }

    String getDateString(){
        return diary_date.getText().toString();
    }

    void setDDayString(String state_string){
        diary_d_day.setText(state_string);
    }

    String getDDayString(){
        return diary_d_day.getText().toString();
    }

}


abstract class PlayButtonOnClickListener implements View.OnClickListener{

    String videoUrl;
    Context thisContext;

    PlayButtonOnClickListener(String videoUrl, Context thisContext){
        this.videoUrl = videoUrl;
        this.thisContext = thisContext;
    }
}
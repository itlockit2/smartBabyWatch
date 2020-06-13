package com.doif.ccbebe.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.doif.ccbebe.DiaryActivity;
import com.doif.ccbebe.R;

public class MainBalloon extends ConstraintLayout {


    private String TAG = "[MBalloon] ";

    Context context;
    ConstraintLayout container;
    ConstraintLayout balloon_button_diary;
    TextView hello;
    TextView birth;
    ImageView photo;

    public MainBalloon(Context context){
        super(context);

        this.context = context;
        initialization();
    }

    public MainBalloon(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        this.context = context;
        initialization();
        getAttributeSet(attributeSet);
    }


    public MainBalloon(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        this.context = context;
        initialization();
        getAttributeSet(attributeSet, defStyle);
    }

    private void initialization(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.balloon_main, this, false);
        addView(v);

        // bind view
        container = findViewById(R.id.balloon_container);
        balloon_button_diary = findViewById(R.id.balloon_button_diary);
        hello =  findViewById(R.id.balloon_hello);
        birth =  findViewById(R.id.balloon_birth);
        photo =  findViewById(R.id.balloon_photo);

        // set photo
        photo.setBackground(new ShapeDrawable(new OvalShape()));

        if(Build.VERSION.SDK_INT >= 21){
            photo.setClipToOutline(true);
        }

        // attach listener
        balloon_button_diary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, DiaryActivity.class);
                context.startActivity(intent);
            }
        });

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

        int container_resId = typedArray.getResourceId(R.styleable.custom_container, R.drawable.balloon_main);
        container.setBackgroundResource(container_resId);

        String hello_string = typedArray.getString(R.styleable.custom_hello);
        hello.setText(hello_string);

        int helloColor = typedArray.getColor(R.styleable.custom_helloColor, 0);
        hello.setTextColor(helloColor);

        String birth_string = typedArray.getString(R.styleable.custom_birth);
        birth.setText(birth_string);

        int birthColor = typedArray.getColor(R.styleable.custom_birthColor, 0);
        birth.setTextColor(birthColor);

        int photo_resId = typedArray.getResourceId(R.styleable.custom_image, R.drawable.ic_launcher_round);
        photo.setImageResource(photo_resId);

        typedArray.recycle();

    }

    void setContainer(int container_resId){
        container.setBackgroundResource(container_resId);
    }

    void setHelloString(String name){

        name = KoreanUtil.getComleteWord(name, "아", "야");
        String hello_string = name + " 안녕!";

        hello.setText(hello_string);
//        hello.setText("Hello BeBe!");

    }

    void setHelloString(String name, boolean isShoot){

        Log.d(TAG, "this is called");

        hello.setText(name);

    }

    void setHelloColor(int helloColor){
        hello.setTextColor(helloColor);
    }

    void setBirthString(String name, String birth_string){

        name = KoreanUtil.getComleteWord(name, "이가", "가");
        birth_string = name + " 태어난지 " + birth_string + "일 되는 날";

        birth.setText(birth_string);
    }

    void setBirthString(String birth_string){
        birth.setText(birth_string);
    }

    void setBirthColor(int birthColor){
        birth.setTextColor(birthColor);
    }

    void setPhoto(String s){
        photo.setImageBitmap(BitmapUtil.stringToBitmap(s));

    }

    void setScaleCenter(){
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }
}
package com.doif.ccbebe.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doif.ccbebe.ChangeTypeActivity;
import com.doif.ccbebe.ContactActivity;
import com.doif.ccbebe.DiaryActivity;
import com.doif.ccbebe.MainActivity;
import com.doif.ccbebe.ManageBabyActivity;
import com.doif.ccbebe.ManageInformActivity;
import com.doif.ccbebe.R;
import com.doif.ccbebe.SettingAlarmActivity;
import com.doif.ccbebe.TimelineActivity;

public class MenuDialog extends Dialog implements View.OnClickListener{

    private String TAG = "[MenuDlg] ";

    private Context context;
    private Activity activity;
    private DisplayMetrics displayMetrics;

    private LinearLayout setting_layout;

    private LinearLayout setting_management_baby_profile;
    private LinearLayout setting_diary;
    private TextView setting_entire_timeline;
    private TextView setting_device;
    private TextView setting_alarm;
    private TextView setting_contact;
    private TextView setting_inform;

    public MenuDialog(Context context, Activity activity, DisplayMetrics displayMetrics){
        super(context);

        this.context = context;
        this.activity = activity;
        this.displayMetrics = displayMetrics;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_menu);

        initialize();
    }

    private void initialize(){
        // bind view
        setting_layout = findViewById(R.id.setting_layout);
        setting_management_baby_profile =findViewById(R.id.setting_management_baby_profile);
        setting_diary = findViewById(R.id.setting_diary);
        setting_entire_timeline = findViewById(R.id.setting_entire_timeline);
        setting_device = findViewById(R.id.setting_device);
        setting_alarm = findViewById(R.id.setting_alarm);
        setting_contact = findViewById(R.id.setting_contact);
        setting_inform = findViewById(R.id.setting_inform);

        // 크기 설정
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) setting_layout.getLayoutParams();
        params.width = (int)(displayMetrics.widthPixels * 0.95);
        setting_layout.setLayoutParams(params);

        // 배경 투명
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);

        // attach listener
        setting_management_baby_profile.setOnClickListener(this);
        setting_diary.setOnClickListener(this);
        setting_entire_timeline.setOnClickListener(this);
        setting_device.setOnClickListener(this);
        setting_alarm.setOnClickListener(this);
        setting_contact.setOnClickListener(this);
        setting_inform.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){

        Class nextClass = MainActivity.class;

        switch (view.getId()){
            case R.id.setting_management_baby_profile:
                nextClass = ManageBabyActivity.class;
                break;
            case R.id.setting_diary:
                nextClass = DiaryActivity.class;
                break;
            case R.id.setting_entire_timeline:
                nextClass = TimelineActivity.class;
                break;
            case R.id.setting_device:
                nextClass = ChangeTypeActivity.class;
                break;
            case R.id.setting_alarm:
                nextClass = SettingAlarmActivity.class;
                break;
            case R.id.setting_contact:
                nextClass = ContactActivity.class;
                break;
            case R.id.setting_inform:
                nextClass = ManageInformActivity.class;
                break;
        }
        Intent intent = new Intent(activity, nextClass);
        activity.startActivity(intent);

        dismiss();
    }

}

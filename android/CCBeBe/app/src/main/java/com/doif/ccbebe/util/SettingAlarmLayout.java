package com.doif.ccbebe.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.doif.ccbebe.R;


public class SettingAlarmLayout extends ConstraintLayout {

    private String TAG = "[SettingAlarmLo] ";

    private TextView layout_setting_alarm_title;
    private TextView layout_setting_alarm_description;
    private Button layout_setting_alarm_button;

    private boolean isChecked;

    public SettingAlarmLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet);
    }

    public SettingAlarmLayout(Context context){
        super(context);
        initialization();
    }

    public SettingAlarmLayout(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet, defStyle);
    }

    private void initialization(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.layout_setting_alarm, this, false);
        addView(v);

        layout_setting_alarm_title = findViewById(R.id.layout_setting_alarm_title);
        layout_setting_alarm_description = findViewById(R.id.layout_setting_alarm_description);
        layout_setting_alarm_button = (Button) findViewById(R.id.layout_setting_alarm_button);

        isChecked = true;

        layout_setting_alarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChecked){
                    layout_setting_alarm_button.setBackgroundResource(R.drawable.bc_alarm_off);
                    isChecked = false;
                }else{
                    layout_setting_alarm_button.setBackgroundResource(R.drawable.bc_alarm_on);
                    isChecked =  true;
                }
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


        String text = typedArray.getString(R.styleable.custom_title);
        layout_setting_alarm_title.setText(text);

        String hint = typedArray.getString(R.styleable.custom_description);
        layout_setting_alarm_description.setHint(hint);

        int container_resId = typedArray.getResourceId(R.styleable.custom_container, R.drawable.bc_alarm_on);
        layout_setting_alarm_button.setBackgroundResource(container_resId);

        typedArray.recycle();

    }

    public boolean getIsChecked(){
        return isChecked;
    }
}

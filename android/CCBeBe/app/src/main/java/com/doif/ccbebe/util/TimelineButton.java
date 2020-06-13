package com.doif.ccbebe.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.doif.ccbebe.R;

public class TimelineButton extends ConstraintLayout {

    private String TAG = "[TLButton] ";

    ConstraintLayout container;
    TextView time;
    TextView state;

    public TimelineButton(Context context){
        super(context);
        initialization();
    }

    public TimelineButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet);
    }

    public TimelineButton(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet, defStyle);
    }

    private void initialization(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.button_timeline, this, false);
        addView(v);

        container = (ConstraintLayout) findViewById(R.id.timeline_container);
        time = (TextView) findViewById(R.id.timeline_time);
        state = (TextView) findViewById(R.id.timeline_state);
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

        int container_resId = typedArray.getResourceId(R.styleable.custom_container, R.drawable.bc_timeline_update);
        container.setBackgroundResource(container_resId);

        String time_string = typedArray.getString(R.styleable.custom_time);
        time.setText(time_string);

        int timeColor = typedArray.getColor(R.styleable.custom_timeColor, 0);
        time.setTextColor(timeColor);

        String state_string = typedArray.getString(R.styleable.custom_state);
        state.setText(state_string);

        int stateColor = typedArray.getColor(R.styleable.custom_stateColor, 0);
        state.setTextColor(stateColor);

        typedArray.recycle();

    }

    void setContainer(int container_resId){
        container.setBackgroundResource(container_resId);
    }

    void setTimeString(String time_string){
        time.setText(time_string);
    }

    String getTimeString(){
        return time.getText().toString();
    }

    void setTimeColor(int timeColor){
        time.setTextColor(timeColor);
    }

    void setStateString(String state_string){
        state.setText(state_string);
    }

    String getStateString(){
        return state.getText().toString();
    }

    void setStateColor(int stateColor){
        state.setTextColor(stateColor);
    }

}
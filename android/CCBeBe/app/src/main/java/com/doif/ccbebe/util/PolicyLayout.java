package com.doif.ccbebe.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.doif.ccbebe.R;

public class PolicyLayout extends ConstraintLayout {

    private String TAG = "[PolicyLo] ";

    private ConstraintLayout container;
    private ImageView checkButton;
    private TextView title;
    private TextView seeMore;

    private boolean isChecked;

    public PolicyLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        initialize();
        getAttributeSet(attributeSet);
    }

    public PolicyLayout(Context context){
        super(context);

        initialize();
    }

    public PolicyLayout(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        initialize();
        getAttributeSet(attributeSet, defStyle);
    }


    private void initialize(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.layout_policy, this, false);
        addView(v);

        // bind view
        container = (ConstraintLayout)findViewById(R.id.layout_policy_container);
        checkButton = (ImageView)findViewById(R.id.check_policy_button);
        title = (TextView)findViewById(R.id.policy_title_text);
        seeMore = (TextView)findViewById(R.id.see_more_button);

        // set check button
        checkButton.setImageResource(R.drawable.ic_unchecked_circle);
        isChecked = false;

        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "clicked");
                if(isChecked == false){
                    checkButton.setImageResource(R.drawable.ic_checked_circle);
                    isChecked = true;
                }else{
                    checkButton.setImageResource(R.drawable.ic_unchecked_circle);
                    isChecked = false;
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

    private void setTypeArray(TypedArray typeArray){
        int container_resId = typeArray.getResourceId(R.styleable.custom_container, R.drawable.bc_policy_layout);
        container.setBackgroundResource(container_resId);

        String text =typeArray.getString(R.styleable.custom_text);
        title.setText(text);

        typeArray.recycle();
    }

    public boolean getIsChecked(){
        return isChecked;
    }

    public ImageView getButton(){
        return checkButton;
    }

    public void setChecked(boolean checked){
        if(checked){
            checkButton.setImageResource(R.drawable.ic_checked_circle);
            isChecked = true;
        }else{
            checkButton.setImageResource(R.drawable.ic_unchecked_circle);
            isChecked = false;
        }
    }

}

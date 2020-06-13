package com.doif.ccbebe.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.doif.ccbebe.R;

public class EasyLoginButton extends FrameLayout {

    FrameLayout container;
    ImageView symbol;
    TextView text;

    public EasyLoginButton(Context context){
        super(context);

        initialization();
    }

    public EasyLoginButton(Context context, AttributeSet attributeSet){

        super(context, attributeSet);

        initialization();
    }

    public EasyLoginButton(Context context, AttributeSet attributeSet, int defStyle){

        super(context, attributeSet, defStyle);
        initialization();
    }

    private void initialization(){

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.button_easy_login, this, false);
        addView(view);

        container = (FrameLayout) findViewById(R.id.container);
        symbol = (ImageView) findViewById(R.id.symbol);
        text = (TextView) findViewById(R.id.text);

    }

}
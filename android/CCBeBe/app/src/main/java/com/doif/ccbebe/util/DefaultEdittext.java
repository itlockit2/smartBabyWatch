package com.doif.ccbebe.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.doif.ccbebe.R;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class DefaultEdittext extends ConstraintLayout {

    private String TAG = "[DefaultEdt] ";

    ConstraintLayout container;
    EditText editText;

    public DefaultEdittext(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet);
    }

    public DefaultEdittext(Context context){
        super(context);
        initialization();
    }

    public DefaultEdittext(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet);

        initialization();
        getAttributeSet(attributeSet, defStyle);
    }

    private void initialization(){

        String infServer = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infServer);
        View v = layoutInflater.inflate(R.layout.edittext_default, this, false);
        addView(v);

        container = (ConstraintLayout) findViewById(R.id.edittext_default_container);
        editText = (EditText) findViewById(R.id.edittext_default_text);

        editText.setTextColor(getResources().getColor(R.color.grayDark));

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if (hasFocus == false){
                    container.setBackgroundResource(R.drawable.bc_edit_text);
                    editText.setTextColor(getResources().getColor(R.color.grayDark));
                }else{
                    container.setBackgroundResource(R.drawable.bc_clicked_edit_text);
                    editText.setTextColor(getResources().getColor(R.color.grayDark));
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

        int container_resId = typedArray.getResourceId(R.styleable.custom_container, R.drawable.bc_edit_text);
        container.setBackgroundResource(container_resId);

        String text = typedArray.getString(R.styleable.custom_text);
        editText.setText(text);

        String hint = typedArray.getString(R.styleable.custom_hint);
        editText.setHint(hint);

        String inputType = typedArray.getString(R.styleable.custom_inputType);

        if (inputType.equals("email"))
            editText.setInputType( TYPE_CLASS_TEXT|TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        else if (inputType.equals("password"))
            editText.setInputType(TYPE_CLASS_TEXT|TYPE_TEXT_VARIATION_PASSWORD);
        else if (inputType.equals("number"))
            editText.setInputType(TYPE_CLASS_NUMBER);
        else if (inputType.equals("phone"))
            editText.setInputType(TYPE_CLASS_PHONE);
        else
            editText.setInputType(TYPE_CLASS_TEXT);

        typedArray.recycle();

    }


    public void setText(String text){
        editText.setText(text);
    }

    public void setHint(String hint){
        editText.setHint(hint);
    }

    public void setInputType(String inputType){
        if (inputType.equals("email"))
            editText.setInputType( TYPE_CLASS_TEXT|TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        else if (inputType.equals("password"))
            editText.setInputType(TYPE_CLASS_TEXT|TYPE_TEXT_VARIATION_PASSWORD);
        else if (inputType.equals("number"))
            editText.setInputType(TYPE_CLASS_NUMBER);
        else if (inputType.equals("phone"))
            editText.setInputType(TYPE_CLASS_PHONE);
        else
            editText.setInputType(TYPE_CLASS_TEXT);
    }

    public String getText(){
        return editText.getText().toString();
    }

}

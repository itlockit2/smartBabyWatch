package com.doif.ccbebe.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.doif.ccbebe.R;
import com.doif.ccbebe.net.SignOutAsyncTask;

public class SignOutDialog extends Dialog{

    private String TAG = "[SignOutDlg] ";

    private Context context;
    private Activity activity;
    private DisplayMetrics displayMetrics;

    private LinearLayout sign_out_dialog_container;
    private Button sign_out_cancel;
    private Button sign_out_admit;

    public SignOutDialog(Context context, Activity activity, DisplayMetrics displayMetrics){
        super(context);

        this.context = context;
        this.activity = activity;
        this.displayMetrics = displayMetrics;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_logout);

        initialize();
    }

    private void initialize(){
        // bind view
        sign_out_dialog_container = findViewById(R.id.sign_out_dialog_container);
        sign_out_cancel = findViewById(R.id.sign_out_cancel);
        sign_out_admit = findViewById(R.id.sign_out_admit);


        // 크기 설정
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sign_out_dialog_container.getLayoutParams();
        params.width = (int)(displayMetrics.widthPixels * 0.9);
        sign_out_dialog_container.setLayoutParams(params);

        // 배경 투명
        getWindow().setBackgroundDrawableResource(R.color.transparent);


        // attach listener
        sign_out_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        sign_out_admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new SignOutAsyncTask(context, activity);
            }
        });

    }

}

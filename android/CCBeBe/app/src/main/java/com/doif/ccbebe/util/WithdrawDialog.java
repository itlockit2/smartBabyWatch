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
import com.doif.ccbebe.net.WithdrawalAsyncTask;

public class WithdrawDialog extends Dialog{

    private String TAG = "[WithdrawDlg] ";

    private Context context;
    private Activity activity;
    private DisplayMetrics displayMetrics;

    private LinearLayout withdraw_dialog_container;
    private Button withdraw_cancel;
    private Button withdraw_admit;

    public WithdrawDialog(Context context, Activity activity, DisplayMetrics displayMetrics){
        super(context);

        this.context = context;
        this.activity = activity;
        this.displayMetrics = displayMetrics;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_withdraw);

        initialize();
    }

    private void initialize(){
        // bind view
        withdraw_dialog_container = findViewById(R.id.withdraw_dialog_container);
        withdraw_cancel = findViewById(R.id.withdraw_cancel);
        withdraw_admit = findViewById(R.id.withdraw_admit);


        // 크기 설정
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) withdraw_dialog_container.getLayoutParams();
        params.width = (int)(displayMetrics.widthPixels * 0.9);
        withdraw_dialog_container.setLayoutParams(params);

        // 배경 투명
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        // attach listener
        withdraw_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        withdraw_admit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
                // DB login 정보 삭제
                new WithdrawalAsyncTask(context, activity);
            }
        });

    }

}

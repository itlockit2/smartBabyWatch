package com.doif.ccbebe;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.util.SignOutDialog;
import com.doif.ccbebe.util.WithdrawDialog;

public class ManageInformActivity extends AppCompatActivity {

    private String TAG = "[MngInfAct] - ";

    private ImageView backFloatingButton_manage_inform;
    private Button save_change_button;
    private TextView sign_out_button;
    private TextView withdrawal_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inform);

        initialize();
    }

    private void initialize() {
        // bind view
        backFloatingButton_manage_inform = findViewById(R.id.backFloatingButton_manage_inform);
        save_change_button = findViewById(R.id.save_change_button);
        sign_out_button = findViewById(R.id.sign_out_button);
        withdrawal_button = findViewById(R.id.withdrawal_button);

        // attach listener
        backFloatingButton_manage_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                new SignOutDialog(ManageInformActivity.this, ManageInformActivity.this, displayMetrics).show();
            }
        });

        withdrawal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                new WithdrawDialog(ManageInformActivity.this, ManageInformActivity.this, displayMetrics).show();

            }
        });

        save_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
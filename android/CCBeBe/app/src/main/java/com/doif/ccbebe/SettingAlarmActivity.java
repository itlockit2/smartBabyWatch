package com.doif.ccbebe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingAlarmActivity extends AppCompatActivity {

    private ImageView backFloatingButton_setting_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        initialize();

        Toast.makeText(getApplicationContext(), "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
    }

    private void initialize(){
        // bind view
        backFloatingButton_setting_alarm = findViewById(R.id.backFloatingButton_setting_alarm);

        // attach listener
        backFloatingButton_setting_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

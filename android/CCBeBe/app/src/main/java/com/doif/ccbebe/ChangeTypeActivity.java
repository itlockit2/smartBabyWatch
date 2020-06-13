package com.doif.ccbebe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ChangeTypeActivity extends AppCompatActivity {

    private String TAG = "[CngTypeAct] ";

    private ImageView backFloatingButton_change_type;
    private ConstraintLayout change_type_monitoring_layout;
    private ConstraintLayout change_type_shooting_layout;
    private ImageView checked_monitoring_image_change;
    private ImageView checked_shooting_image_change;
    private TextView change_type_description;
    private Button change_type_start_button;

    private String isStreamer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_type);

        initialize();
    }

    private void initialize(){

        // initilize
        SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
        isStreamer = sharedPreferences.getString("isStreamer", "false");

        // bind view
        backFloatingButton_change_type = findViewById(R.id.backFloatingButton_change_type);
        change_type_monitoring_layout = findViewById(R.id.change_type_monitoring_layout);
        change_type_shooting_layout = findViewById(R.id.change_type_shooting_layout);
        checked_monitoring_image_change = findViewById(R.id.checked_monitoring_image_change);
        checked_shooting_image_change = findViewById(R.id.checked_shooting_image_change);
        change_type_description = findViewById(R.id.change_type_description);
        change_type_start_button = findViewById(R.id.change_type_start_button);
        
        // set button background
        if(isStreamer.equals("true")){
            change_type_description.setText(getString(R.string.type_now_status_shooting));

            change_type_monitoring_layout.setBackgroundResource(R.drawable.bc_edit_text);
            change_type_shooting_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);

            checked_monitoring_image_change.setVisibility(View.GONE);
            checked_shooting_image_change.setVisibility(View.VISIBLE);
        }else{
            change_type_description.setText(getString(R.string.type_now_status_monitoring));

            change_type_monitoring_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);
            change_type_shooting_layout.setBackgroundResource(R.drawable.bc_edit_text);

            checked_monitoring_image_change.setVisibility(View.VISIBLE);
            checked_shooting_image_change.setVisibility(View.GONE);
        }

        // attach listener
        backFloatingButton_change_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_type_monitoring_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_type_monitoring_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);
                change_type_shooting_layout.setBackgroundResource(R.drawable.bc_edit_text);

                checked_monitoring_image_change.setVisibility(View.VISIBLE);
                checked_shooting_image_change.setVisibility(View.GONE);

                isStreamer = "false";

            }
        });

        change_type_shooting_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                change_type_monitoring_layout.setBackgroundResource(R.drawable.bc_edit_text);
                change_type_shooting_layout.setBackgroundResource(R.drawable.bc_clicked_edit_text);

                checked_monitoring_image_change.setVisibility(View.GONE);
                checked_shooting_image_change.setVisibility(View.VISIBLE);

                isStreamer = "true";
            }
        });

        change_type_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("isStreamer", isStreamer);

                editor.commit();

                if (isStreamer.equals("true")) {
                    Toast.makeText(getApplicationContext(), "촬영 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "모니터링 모드로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(ChangeTypeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}

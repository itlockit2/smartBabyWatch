package com.doif.ccbebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.util.PolicyLayout;

public class PolicyActivity extends AppCompatActivity {

    private ImageView backFloatingButton_policy;
    private Button policy_next_button;

    private PolicyLayout agree_service_layout;
    private PolicyLayout agree_policy_layout;
    private PolicyLayout agree_all_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        initialize();
    }

    private void initialize(){
        // bind view
        backFloatingButton_policy = findViewById(R.id.backFloatingButton_policy);
        policy_next_button = findViewById(R.id.policy_next_button);

        agree_service_layout = findViewById(R.id.agree_service_layout);
        agree_policy_layout = findViewById(R.id.agree_policy_layout);
        agree_all_layout = findViewById(R.id.agree_all_layout);

        // attach listener
        backFloatingButton_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        agree_service_layout.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agree_service_layout.setChecked(!agree_service_layout.getIsChecked());
                if(agree_service_layout.getIsChecked() && agree_policy_layout.getIsChecked())
                    agree_all_layout.setChecked(true);
                else
                    agree_all_layout.setChecked(false);
            }
        });

        agree_policy_layout.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agree_policy_layout.setChecked(!agree_policy_layout.getIsChecked());
                if(agree_service_layout.getIsChecked() && agree_policy_layout.getIsChecked())
                    agree_all_layout.setChecked(true);
                else
                    agree_all_layout.setChecked(false);
            }
        });

        agree_all_layout.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agree_all_layout.setChecked(!agree_all_layout.getIsChecked());
                agree_policy_layout.setChecked(agree_all_layout.getIsChecked());
                agree_service_layout.setChecked(agree_all_layout.getIsChecked());
            }
        });

        agree_all_layout.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agree_all_layout.setChecked(!agree_all_layout.getIsChecked());
                agree_policy_layout.setChecked(agree_all_layout.getIsChecked());
                agree_service_layout.setChecked(agree_all_layout.getIsChecked());
            }
        });

        policy_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check agree
                boolean isCheckedService = agree_service_layout.getIsChecked();
                boolean isCheckedPolicy = agree_policy_layout.getIsChecked();
                boolean isCheckedAll = agree_all_layout.getIsChecked();

                if(!isCheckedService|!isCheckedPolicy|!isCheckedAll){
                    Toast.makeText(getApplicationContext(), "이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(PolicyActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

package com.doif.ccbebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessSignUpActivity extends AppCompatActivity {

    private ImageView backFloatingButton_success_sign_up;
    private Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_sign_up);

        initialize();
    }

    private void initialize(){

        // bind view
        backFloatingButton_success_sign_up = findViewById(R.id.backFloatingButton_success_sign_up);
        start_button = findViewById(R.id.start_button);

        // attach listener
        backFloatingButton_success_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessSignUpActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

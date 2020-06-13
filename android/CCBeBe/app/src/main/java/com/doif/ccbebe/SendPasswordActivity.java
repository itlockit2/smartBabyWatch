package com.doif.ccbebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SendPasswordActivity extends AppCompatActivity {

    private ImageView backFloatingButton_send_password;
    private Button send_email_login_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_password);

        initialize();
    }

    private void initialize(){
        // bind view
        backFloatingButton_send_password = findViewById(R.id.backFloatingButton_send_password);
        send_email_login_button = findViewById(R.id.send_email_login_button);

        // attach listener
        backFloatingButton_send_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send_email_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendPasswordActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

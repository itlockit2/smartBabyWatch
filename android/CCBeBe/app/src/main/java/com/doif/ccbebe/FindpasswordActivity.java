package com.doif.ccbebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FindpasswordActivity extends AppCompatActivity {

    private ImageView backFloatingButton_find_password;

    private Button find_password_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        initialize();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(FindpasswordActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialize(){
        // bind view
        backFloatingButton_find_password = findViewById(R.id.backFloatingButton_find_password);
        find_password_button = findViewById(R.id.find_password_button);

        // attach listener
        backFloatingButton_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(FindpasswordActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });

        find_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FindpasswordActivity.this, SendPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

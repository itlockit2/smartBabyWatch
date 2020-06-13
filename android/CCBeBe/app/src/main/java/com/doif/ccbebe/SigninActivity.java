package com.doif.ccbebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.net.LoginAsyncTask;
import com.doif.ccbebe.util.DefaultEdittext;

public class SigninActivity extends AppCompatActivity {

    private ImageView backFloatingButton_signin;
    private TextView forgot_email_button;
    private Button signin_button;

    private DefaultEdittext sign_in_email_edittext;
    private DefaultEdittext sign_in_password_edittext;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initialize();
    }

    private void initialize(){
        // bind view
        backFloatingButton_signin = findViewById(R.id.backFloatingButton_signin);
        forgot_email_button = findViewById(R.id.forgot_email_button);
        signin_button = findViewById(R.id.signin_button);
        sign_in_email_edittext = findViewById(R.id.sign_in_email_edittext);
        sign_in_password_edittext = findViewById(R.id.sign_in_password_edittext);

        // attach listener
        backFloatingButton_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = sign_in_email_edittext.getText();
                password = sign_in_password_edittext.getText();

                if(email.length() == 0){
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(password.length() == 0){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    new LoginAsyncTask(SigninActivity.this, SigninActivity.this, email, password);
                }
            }
        });

        forgot_email_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){

                Intent intent = new Intent(SigninActivity.this, FindpasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

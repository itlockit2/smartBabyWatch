package com.doif.ccbebe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doif.ccbebe.net.CreateUserAsyncTask;
import com.doif.ccbebe.util.DefaultEdittext;

public class SignupActivity extends AppCompatActivity {

    private ImageView backFloatingButton_signup;
    private Button signup_button;

    private DefaultEdittext email_edittext;
    private DefaultEdittext password_edittext;
    private DefaultEdittext check_password_edittext;

    private String email;
    private String password;
    private String checked_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initialize();

    }

    private void initialize(){
        email = null;
        password = null;
        checked_password = null;
        // bind view
        backFloatingButton_signup = findViewById(R.id.backFloatingButton_signup);
        signup_button = findViewById(R.id.signup_button);
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        check_password_edittext = findViewById(R.id.check_password_edittext);

        // attach listener
        backFloatingButton_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_edittext.getText();
                password = password_edittext.getText();
                checked_password = check_password_edittext.getText();

                if(email.length() == 0) Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if(password.length() == 0) Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if(checked_password.length() == 0) Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                else{
                    if(!password.equals(checked_password)){
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new CreateUserAsyncTask(SignupActivity.this, SignupActivity.this, email, password);
                    }
                }
            }
        });

    }

}

package com.doif.ccbebe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private String TAG = "[ContactActSend] ";

    private ImageView backFloatingButton_contact;
    private EditText contact_title;
    private EditText contact_contents;
    private Button contact_button;

    private String title;
    private String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initialize();

        Toast.makeText(getApplicationContext(), "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
    }

    private void initialize(){

        // bind view
        backFloatingButton_contact = findViewById(R.id.backFloatingButton_contact);
        contact_title = findViewById(R.id.contact_title);
        contact_contents = findViewById(R.id.contact_contents);
        contact_button = findViewById(R.id.contact_button);

        // attatch listener
        backFloatingButton_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = contact_title.getText().toString();
                contents = contact_contents.getText().toString();

                Log.d(TAG, "title - " + title);
                Log.d(TAG, "contents 0 " + contents);

                Toast.makeText(getApplicationContext(), "문의가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

package com.doif.ccbebe;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doif.ccbebe.util.DiaryRecyclerviewAdapter;

public class DiaryActivity extends AppCompatActivity {

    private String TAG = "[DiaryAct] ";

    private ImageView backFloatingButton_diary;
    private RecyclerView diary_recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        initialize();

        Toast.makeText(getApplicationContext(), "추후 구현 예정입니다.", Toast.LENGTH_SHORT).show();
    }

    private void initialize(){

        // bind view
        backFloatingButton_diary = findViewById(R.id.backFloatingButton_diary);
        diary_recyclerview = findViewById(R.id.diary_recyclerview);

        // set recyclerview
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        diary_recyclerview.setLayoutManager(new LinearLayoutManager(DiaryActivity.this));
        diary_recyclerview.setAdapter(new DiaryRecyclerviewAdapter(DiaryActivity.this, metrics));

        // attach listener
        backFloatingButton_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}

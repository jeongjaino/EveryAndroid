package com.example.studywithtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {

    private TextView todoText;
    private ImageButton backButton;
    private ImageButton menuButton;

    DataBaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        todoText = (TextView) findViewById(R.id.todo_editText);
        backButton = (ImageButton) findViewById(R.id.back_button);
        menuButton = (ImageButton) findViewById(R.id.menu_button);

        todoText.setText(getIntent().getStringExtra("todo"));

        helper = new DataBaseHelper(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = todoText.getText().toString();

                // 공백으로 저장시 이전의 텍스트로 저장
                if(text.equals("")) {
                    text = getIntent().getStringExtra("todo");
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요",Toast.LENGTH_SHORT).show();
                }

                helper.TodoUpdateData(
                        getIntent().getIntExtra("position", 0),
                        text,
                        getIntent().getIntExtra("checked", 0)
                );

                Intent writeIntent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(writeIntent);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.TodoDeleteData(getIntent().getIntExtra("position", 0));
                Intent writeIntent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(writeIntent);
            }
        });
    }
}
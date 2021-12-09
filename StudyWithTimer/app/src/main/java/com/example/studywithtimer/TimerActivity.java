package com.example.studywithtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studywithtimer.adapter.TodoAdapter;
import com.example.studywithtimer.dataclass.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements TodoAdapter.OnItemClickListener{

    private TimerService timerService;

    private final Handler TimerHandler = new UiUpdateHandler(this);

    private boolean serviceStatus;

    private TextView timerText;
    private EditText todoText;
    private ImageButton stopButton;
    private ImageButton writeButton;
    private ImageButton doneButton;
    private ImageButton exitButton;
    private CardView todoCardView;

    DataBaseHelper helper;

    TodoAdapter todoAdapter;
    ArrayList<TodoItem> todoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        helper = new DataBaseHelper(this);

        sendCommandToService();

        selectData();

        timerText = (TextView)findViewById(R.id.timerText);
        todoText = (EditText)findViewById(R.id.todo_text);

        stopButton = (ImageButton)findViewById(R.id.stop_button);
        writeButton = (ImageButton) findViewById(R.id.todo_write_button);
        doneButton = (ImageButton) findViewById(R.id.done_button);
        exitButton = (ImageButton) findViewById(R.id.exit_button);

        todoCardView = (CardView) findViewById(R.id.todo_cardView);
        ListView todoListView = (ListView)findViewById(R.id.todoListView);

        todoCardView.setVisibility(View.GONE);

        todoListView.setAdapter(todoAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 E요일", Locale.KOREA);
        stopButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                Intent timerIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(timerIntent);

                String date = sdf.format(System.currentTimeMillis());
                String elapsedTime = timerText.getText().toString();
                String startTime = timerService.returnTime(true);
                String endTime = timerService.returnTime(false);

                helper.timeTableInsertData(date, elapsedTime, startTime, endTime);
            }
        });
        writeButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateTodoUi(true);
            }
        });
        exitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateTodoUi(false);
            }
        });
            doneButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateTodoUi(false);
                String text = todoText.getText().toString();
                if(!text.equals("")) {
                    helper.TodoInsertData(text, 0);
                    todoText.setText("");
                    selectData();
                    todoListView.setAdapter(todoAdapter);
                }
                else{
                    Toast.makeText(getApplicationContext(),"내용을 입력하세요.", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    public void updateTodoUi(Boolean isWriting){
        if(isWriting){
            todoCardView.setVisibility(View.VISIBLE); //animation
            writeButton.setVisibility(View.GONE);
        }
        else{
            todoCardView.setVisibility(View.GONE); //ani
            writeButton.setVisibility(View.VISIBLE);
        }
    }
    private void stopTimer(){
        updateStopUi();
        timerService.stopService();
        unbindService(mConnection);
        serviceStatus = false;
        //db에 time 저장
    }

    private void updateStartUi(){
        TimerHandler.sendEmptyMessage(0);
    }
    private void updateStopUi(){
        TimerHandler.sendEmptyMessage(0);
    }
    private void updateUiTimer(){
        if(serviceStatus){
            timerText.setText(timerService.elapsedTime());
        }
    }
    private void sendCommandToService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else startService(serviceIntent);
        bindService(serviceIntent, mConnection, 0);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            TimerService.ServiceBinder binder = (TimerService.ServiceBinder) service;
            timerService = binder.getService();
            serviceStatus = true;
            if(timerService.serviceIsRunning()){
                updateStartUi();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceStatus = false;
        }
    };

    //handler는 어느 한 activity에 종속 되어서는 안됨
    //static으로 선언후 weakreference 사용하여 activity에 접근
    private static class UiUpdateHandler extends Handler{

        private final WeakReference<TimerActivity> activity;

        UiUpdateHandler(TimerActivity activity){
            this.activity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (0 == msg.what) {
                activity.get().updateUiTimer();
                sendEmptyMessageDelayed(0, 1000);
            }
        }
    }
    public void selectData() {
        todoArrayList = helper.TodoSelectData();

        todoAdapter = new TodoAdapter(this, todoArrayList, this);
        todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCardViewClickListener(int position) {
        Intent todoIntent = new Intent(this, WriteActivity.class);
        todoIntent.putExtra("todo", helper.todoLoadData(position).getText());
        todoIntent.putExtra("checked", helper.todoLoadData(position).getChecked());
        todoIntent.putExtra("position",position);
        startActivity(todoIntent);
    }

    @Override
    public void onCheckBoxClickListener(int position, String text, int checked) {
        int check = 0;
        Log.d("currentTag",String.valueOf(checked));
        if(checked == 0){
            check = 1;
        }
        /*
        if(helper.TodoUpdateData(position, text, check)){
            Log.d("tag","updateSuccess");
        }else{
            Log.d("tag","updatefailed");
        }*/
        Log.d("future tag",String.valueOf(helper.TodoUpdateData(position, text, check )));
        selectData();
    }
}



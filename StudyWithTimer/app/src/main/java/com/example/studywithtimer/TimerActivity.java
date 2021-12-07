package com.example.studywithtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private TimerService timerService;

    private final Handler TimerHandler = new UiUpdateHandler(this);

    private boolean serviceStatus;

    private TextView timerText;
    private FloatingActionButton stopButton;

    SQLiteDatabase db;
    String timeDataBase = "TimeDataBase";
    String todoTable = "ToDoTable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        openTimeDB();

        sendCommandToService("START_TIMER");

        timerText = (TextView)findViewById(R.id.timerText);
        stopButton = (FloatingActionButton)findViewById(R.id.stopButton);

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

                timeTableInsertData(date, elapsedTime, startTime, endTime);
            }
        });
    }
    private void stopTimer(){
        updateStopUi();
        sendCommandToService("STOP_TIMER");
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
            timerText.setText(timerService.currentTime());
        }
    }
    private void sendCommandToService(String action){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.setAction(action);
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

    public void openTimeDB(){
        try{
            db = openOrCreateDatabase(
                    timeDataBase,
                    Activity.MODE_PRIVATE,
                    null
            );
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void timeTableInsertData(String date, String time, String startTime, String endTime) {
        try {
            if (db != null) {
                String sql = "insert into TimeTable(date, time, startTime, endTime) values(?, ?, ?, ?)";
                Object[] params = {date, time, startTime, endTime};
                db.execSQL(sql, params);
                Log.d("tag","success");
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("tag","false");
        }
    }
    /*
    public void createTodoTable(){
        db.execSQL("create table if not exists " + toDoTableName + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " todo text);"
        );
    }
    public void TodoInsertData(String name, int age, String number) {
        int resId = R.drawable.singer;
        try {
            if (db != null) {
                String sql = "insert into NumberTable(name, number, age, resId) values(?, ?, ?, ?)";
                Object[] params = {name, number, age, resId};
                db.execSQL(sql, params);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}



package com.example.studywithtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;

public class TimerActivity extends AppCompatActivity {

    private TimerService timerService;

    private final Handler TimerHandler = new UiUpdateHandler(this);

    private boolean serviceStatus;

    private TextView timerText;
    private FloatingActionButton stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        sendCommandToService("START_TIMER");

        timerText = (TextView)findViewById(R.id.timerText);
        stopButton = (FloatingActionButton)findViewById(R.id.stopButton);

        stopButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopTimer();
                Intent timerIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(timerIntent);
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        stopTimer();
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
            timerText.setText(timerService.currentTime() + "seconds");
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

}

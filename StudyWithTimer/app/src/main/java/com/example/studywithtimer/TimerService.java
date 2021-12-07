package com.example.studywithtimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

public class TimerService extends Service {


    private static final int NOTIFICATION_ID = 2021;
    private static final String TIMER_NOTIFICATION_ID = "12476";

    private boolean timerRunning;

    private long startTime, endTime;


    public class ServiceBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private final IBinder serviceBinder = new ServiceBinder();

    @Override
    public void onCreate() {
        startTime = 0;
        endTime = 0;
        timerRunning = false;
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("START_TIMER")) {
            startService();
        } else if (intent.getAction().equals("STOP_TIMER")) {
            stopService();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public void startService() {
        if (!timerRunning) {
            timerRunning = true;
            startTime = System.currentTimeMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(NOTIFICATION_ID, createNotification());
            }
        }
    }

    public void stopService() {
        if (timerRunning) {
            timerRunning = false;
            endTime = System.currentTimeMillis();
            stopForeground(true);
            stopSelf();
        }
    }

    public boolean serviceIsRunning() {
        return timerRunning;
    }

    public String currentTime() {
        Long seconds;
        seconds = endTime > startTime ?
                (endTime - startTime) / 100 :
                (System.currentTimeMillis() - startTime) / 1000;
        return timerUtils(seconds);
    }

    public String timerUtils(Long time){
        Long seconds = time;
        Long hours = TimeUnit.SECONDS.toHours(seconds);
        seconds -= TimeUnit.HOURS.toSeconds(hours);
        Long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        String zero = "0";

        return  setTime(hours) +": "+ setTime(minutes) + ": " + setTime(seconds);
    }

    public String setTime(Long time){
        if(time < 10){
            return "0"+time;
        }
        else{
            return ""+time;
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                    TIMER_NOTIFICATION_ID, "Study With Timer Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, TIMER_NOTIFICATION_ID)
                .setContentTitle("Study With Timer")
                .setContentText("시간 측정중")
                .setSmallIcon(R.drawable.ic_baseline_timer_24);

        Intent serviceIntent = new Intent(getApplicationContext(), TimerActivity.class);

        PendingIntent timePendingIntent =
                PendingIntent.getActivity(this, 0,
                        serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(timePendingIntent);

        return builder.build();
    }
}
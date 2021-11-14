package com.example.databasepush

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.databasepush.Constants.Companion.NOTIFICATION_ID
import kotlin.random.Random

private const val CHANNEL_ID = "alarm_channel"

class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        val intent = Intent(context, MainActivity::class.java)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0 ,intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText("Todo Notification")
            .setSmallIcon(R.drawable.penguin)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

    }
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW).apply{
            description = "My channel description"
            enableLights(true)
            lightColor = Color.LTGRAY
        }
        notificationManager.createNotificationChannel(channel)
    }
}
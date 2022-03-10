package com.example.messagenotification
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val type = remoteMessage.data["type"]?.let{
            NotificationType.valueOf(it)
        //value of -> enum class 에서 it과 동일한 클래스를 가져옴
        }
        val text = remoteMessage.data["message"]
        val title = remoteMessage.data["title"]

        type ?: return //type이 null일 경우 return 알림 실행x

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, text))
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }
    private fun createNotification(
        type: NotificationType, title: String?, text: String?
    ): Notification {

        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra("NotificationType", "${    type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)


        val notificationBuilder =  NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when(type){
            NotificationType.NORMAL -> Unit

            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "hello world," +
                                    "wellcome to jaino world"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder.setStyle(
                    NotificationCompat.DecoratedCustomViewStyle()
                )
                    .setCustomContentView(RemoteViews(
                        packageName,
                        R.layout.new_custom_notification
                        ).apply{
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.text, text)
                    }
                    )
            }
        }
        return notificationBuilder.build()
    }
    companion object{
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji channel"
        private const val CHANNEL_ID = "Channel id"
    }
}
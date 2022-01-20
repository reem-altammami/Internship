package com.reem.internship

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "notification_name"


class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("hussain", "onMessageReceived: in in asdfakjd")
        if (remoteMessage.getNotification() != null) {
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!
            )
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.reem.internship", R.layout.notification)

        remoteView.setTextViewText(R.id.notificationTitle, title)
        remoteView.setTextViewText(R.id.notificationText, message)
        remoteView.setImageViewResource(R.id.notificationImage, R.drawable.ic_launcher_foreground)

        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // FLAG_ONE_SHOT means we will use pendingIntent only once
        // opening content of the notification using pendingIntent
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title, message))

        // Enter the channel with the system
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.e("hussain", "generateNotification: out  start")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("hussain", "generateNotification: start")
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

}
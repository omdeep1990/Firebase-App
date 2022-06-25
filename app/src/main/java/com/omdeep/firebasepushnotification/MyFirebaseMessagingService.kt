package com.omdeep.firebasepushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification channel"
const val channelName = "com.omdeep.firebasepushnotification"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {


    //TODO: Generate the notification and attach the notification created with the custom layout and then show the notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String) : RemoteViews {
        val remoteView = RemoteViews("com.omdeep.firebasepushnotification", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.notification)

        return remoteView
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        //TODO: Clears all the activities and all the activity stacks and put this current activity at the top
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //TODO: 'PendingIntent.FLAG_ONE_SHOT indicates' :We have to do this pending activity at once
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //TODO: Channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notification)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(3000, 3000, 3000, 3000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}
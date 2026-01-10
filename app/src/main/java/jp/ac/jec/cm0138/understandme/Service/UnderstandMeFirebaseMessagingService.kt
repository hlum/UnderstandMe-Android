package jp.ac.jec.cm0138.understandme.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jp.ac.jec.cm0138.understandme.MainActivity
import jp.ac.jec.cm0138.understandme.R

class UnderstandMeFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        const val CHANNEL_ID = "understand_me_notifications"
        private const val CHANNEL_NAME = "UnderstandMe Notifications"
        const val EXTRA_HOMEWORK_ID = "homeworkId"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived called")
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Data: ${remoteMessage.data}")
        Log.d(TAG, "Notification: ${remoteMessage.notification}")

        val data = remoteMessage.data
        val homeworkId = data["homeworkId"]

        // Get title and body from notification payload or data payload
        val title = remoteMessage.notification?.title 
            ?: data["title"] 
            ?: "UnderstandMe"
        val body = remoteMessage.notification?.body 
            ?: data["body"] 
            ?: ""

        Log.d(TAG, "Homework ID: $homeworkId")
        Log.d(TAG, "Showing notification - Title: $title, Body: $body")

        showNotification(
            title = title,
            body = body,
            homeworkId = homeworkId
        )
    }

    private fun showNotification(title: String, body: String, homeworkId: String?) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java).apply {
            action = "OPEN_HOMEWORK_DETAIL"
            addCategory(Intent.CATEGORY_DEFAULT)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            homeworkId?.let { putExtra(EXTRA_HOMEWORK_ID, it) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            homeworkId?.hashCode() ?: System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .build()

        Log.d(TAG, "Calling notificationManager.notify()")
        notificationManager.notify(homeworkId?.hashCode() ?: System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for homework updates"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
                Log.d(TAG, "Notification channel created")
            }
        }
    }
}

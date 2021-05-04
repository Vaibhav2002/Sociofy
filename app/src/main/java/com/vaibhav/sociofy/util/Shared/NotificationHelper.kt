package com.vaibhav.sociofy.util.Shared

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.vaibhav.sociofy.R
import javax.inject.Inject
import kotlin.random.Random

class NotificationHelper @Inject constructor(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "SOCIOFY_ID"
        const val CHANNEL_NAME = "SOCIOFY_CHANNEL"

    }

    private fun getNotificationId() = Random.nextInt()

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNotificationChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

    }

    fun showNotification(title: String, description: String, pendingIntent: PendingIntent) {
        val notification =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build()
            } else {
                NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build()
            }
        notificationManager.notify(getNotificationId(), notification)

    }


}
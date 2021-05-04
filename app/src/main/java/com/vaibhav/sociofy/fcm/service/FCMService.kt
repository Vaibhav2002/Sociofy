package com.vaibhav.sociofy.fcm.service

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.ui.HomeScreen.HomeActivity
import com.vaibhav.sociofy.util.Shared.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FCMService :
    FirebaseMessagingService() {


    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onNewToken(p0: String) {
        Timber.d("InService $p0")
        super.onNewToken(p0)
//        authRepository.addUserTokenToFireStore(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Timber.d(p0.data.toString())
        val data = p0.data
        val name = data["title"] ?: "someone"
        val description = data["description"] ?: "Image got posted"
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        notificationHelper.showNotification(name, description, pendingIntent)

    }

}
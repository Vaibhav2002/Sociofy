package com.vaibhav.sociofy.fcm.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.vaibhav.sociofy.data.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FCMService :
    FirebaseMessagingService() {


    @Inject
    lateinit var authRepository: AuthRepository

    override fun onNewToken(p0: String) {
        Timber.d("InService $p0")
        super.onNewToken(p0)
        authRepository.addUserTokenToFireStore(p0)
    }

}
package com.vaibhav.sociofy.di

import android.content.Context
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun providesStorage() = Firebase.storage

    @Provides
    @Singleton
    fun providesMessaging() = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun providesGlide(@ApplicationContext context: Context) = Glide.with(context)

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("SOCIOFY", Context.MODE_PRIVATE)

    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context) = context
}
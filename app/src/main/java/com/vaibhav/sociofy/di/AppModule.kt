package com.vaibhav.sociofy.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.vaibhav.sociofy.data.local.ImageTypeConverter
import com.vaibhav.sociofy.data.local.PostDao
import com.vaibhav.sociofy.data.local.PostDataBase
import com.vaibhav.sociofy.data.remote.Api
import com.vaibhav.sociofy.util.FcmUtils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun providesRoom(@ApplicationContext context: Context): PostDataBase = Room.databaseBuilder(
        context, PostDataBase::class.java, "SociofyDB"
    )
        .fallbackToDestructiveMigration()
        .addTypeConverter(ImageTypeConverter())
        .build()

    @Provides
    @Singleton
    fun providesPostDao(db: PostDataBase): PostDao = db.getDao()

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("SOCIOFY", Context.MODE_PRIVATE)

    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}
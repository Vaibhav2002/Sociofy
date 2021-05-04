package com.vaibhav.sociofy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vaibhav.sociofy.data.models.local.DownloadedPost

@Database(entities = [DownloadedPost::class], version = 2, exportSchema = false)
@TypeConverters(ImageTypeConverter::class)
abstract class PostDataBase : RoomDatabase() {

    abstract fun getDao(): PostDao
}
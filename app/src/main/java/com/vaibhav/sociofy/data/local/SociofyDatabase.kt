package com.vaibhav.sociofy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vaibhav.sociofy.data.local.downloadedPost.DownloadedPostDao
import com.vaibhav.sociofy.data.local.downloadedPost.ImageTypeConverter
import com.vaibhav.sociofy.data.models.local.DownloadedPost

@Database(entities = [DownloadedPost::class], version = 3, exportSchema = false)
@TypeConverters(ImageTypeConverter::class)
abstract class SociofyDatabase : RoomDatabase() {

    abstract fun getDownloadedPostDao(): DownloadedPostDao

}
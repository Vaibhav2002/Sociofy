package com.vaibhav.sociofy.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.annotation.RequiresApi
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


@ProvidedTypeConverter
class ImageTypeConverter {


    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return encodeToString(b, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(encodedString: String): Bitmap {
        val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}
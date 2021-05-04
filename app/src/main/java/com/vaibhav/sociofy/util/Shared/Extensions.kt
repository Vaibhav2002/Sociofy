package com.vaibhav.sociofy.util.Shared

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.net.URL
import java.nio.ByteBuffer


@ExperimentalCoroutinesApi
fun urlToBitmap(context: Context, url: String) =
    callbackFlow<Bitmap> {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    offer(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

        awaitClose {}
    }


fun Bitmap.convertToByteArray(): ByteArray {
    //minimum number of bytes that can be used to store this bitmap's pixels
    val size = this.byteCount

    //allocate new instances which will hold bitmap
    val buffer = ByteBuffer.allocate(size)
    val bytes = ByteArray(size)

    //copy the bitmap's pixels into the specified buffer
    this.copyPixelsToBuffer(buffer)

    //rewinds buffer (buffer position is set to zero and the mark is discarded)
    buffer.rewind()

    //transfer bytes from buffer into the given destination array
    buffer.get(bytes)

    //return bitmap's pixels
    return bytes
}


suspend fun Context.convertToBitmap(url: String): Bitmap {
    val loader = ImageLoader(this)
    val request = ImageRequest.Builder(this)
        .data(url)
        .allowHardware(false) // Disable hardware bitmaps.
        .build()

    val result = (loader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}

suspend fun convertUrlToBitmap(image_url: String): Bitmap = withContext(Dispatchers.IO) {
    val url = URL(image_url)
    return@withContext BitmapFactory.decodeStream(url.openConnection().getInputStream())
}

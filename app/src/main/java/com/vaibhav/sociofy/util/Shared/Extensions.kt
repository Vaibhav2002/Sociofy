package com.vaibhav.sociofy.util.Shared

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


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


fun Context.showDeleteDialog(
    title: String,
    message: String,
    onDeletePressed: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("DELETE") { dialogInterface: DialogInterface, i: Int ->
            onDeletePressed()
        }
        .setNegativeButton("CANCEL", null)
        .show()

}
package com.vaibhav.sociofy.util

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.remote.Notification

@BindingAdapter("setProfileImage")
fun ImageView.setProfileImage(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .error(R.drawable.blankuserimg)
        .into(this)
}

@BindingAdapter("setPostImage")
fun ImageView.setPostImage(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .into(this)
}

@BindingAdapter("setDownloadedProfileImage")
fun ImageView.setDownloadedProfileImage(bitmap: Bitmap) {
    Glide.with(this)
        .load(bitmap)
        .centerCrop()
        .error(R.drawable.blankuserimg)
        .into(this)
}

@BindingAdapter("setDownloadedPostImage")
fun ImageView.setDownloadedPostImage(bitmap: Bitmap) {
    Glide.with(this)
        .load(bitmap)
        .centerCrop()
        .into(this)
}


@BindingAdapter("setTimeElapsed")
fun TextView.setTimeElapsed(postTime: Long) {
    text = getTimeElapsed(postTime)
}

private fun getTimeElapsed(time: Long): String {
    val currentTime = System.currentTimeMillis();
    var timePassed = currentTime - time;
    timePassed = timePassed / 1000 / 60;
    var timeFormatted = "";
    timeFormatted = if (timePassed < 60)
        "${timePassed}mins ago"
    else if (timePassed < 24 * 60)
        "${timePassed / 60}hrs ago"
    else
        "${timePassed / 60 / 24}days ago"
    return timeFormatted
}

@BindingAdapter("setNotificationText")
fun TextView.setNotificationText(notification: Notification) {
    val notifytext =
        "${notification.username} posted a pic ${getTimeElapsed(notification.timestamp)}"
    text = notifytext
}

@BindingAdapter("setLikes")
fun TextView.setLikes(likesCount: Int) {
    val likes = likesCount.toDouble()
    if (likes == 0.0)
        text = ""
    else if (likes == 1.0)
        text = "1 like"
    else if (likes >= 1000000)
        text = "${likes / 1000000}M likes"
    else if (likes >= 1000)
        text = "${likes / 1000}K likes"
    else
        text = "${likes.toInt()} likes"
}


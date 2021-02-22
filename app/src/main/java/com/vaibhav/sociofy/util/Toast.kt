package com.vaibhav.sociofy.util

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.vaibhav.sociofy.R
import www.sanju.motiontoast.MotionToast


fun showSuccessToast(context: Context, activity: Activity, message: String) {
    if (checkDarkMode(context))
        showSuccessToastDark(activity, message)
    else
        showSuccessToastLight(activity, message)
}

fun showErrorToast(context: Context, activity: Activity, message: String) {
    if (checkDarkMode(context))
        showErrorToastDark(activity, message)
    else
        showErrorToastLight(activity, message)
}

private fun showSuccessToastLight(activity: Activity, message: String) {
    MotionToast.createColorToast(
        activity,
        "Upload Completed!",
        message,
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(activity, R.font.poppins)
    )
}

private fun showErrorToastLight(activity: Activity, message: String) {
    MotionToast.createColorToast(
        activity,
        "Upload Completed!",
        message,
        MotionToast.TOAST_ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(activity, R.font.poppins)
    )
}

private fun showSuccessToastDark(activity: Activity, message: String) {
    MotionToast.darkToast(
        activity,
        "Upload Completed!",
        message,
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(activity, R.font.poppins)
    )
}

private fun showErrorToastDark(activity: Activity, message: String) {
    MotionToast.darkToast(
        activity,
        "Upload Completed!",
        message,
        MotionToast.TOAST_ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(activity, R.font.poppins)
    )
}
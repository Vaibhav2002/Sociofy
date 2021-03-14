package com.vaibhav.sociofy.util

import android.content.Context
import android.content.res.Configuration

fun checkDarkMode(
    context: Context
) = context.resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
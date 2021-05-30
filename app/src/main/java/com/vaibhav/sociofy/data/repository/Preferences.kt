package com.vaibhav.sociofy.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.util.Constants
import com.vaibhav.sociofy.util.checkDarkMode
import javax.inject.Inject

class Preferences @Inject constructor(
    private val context: Context,
    private val sharedPref: SharedPreferences
) {


    fun setThemeMode(theme: Constants.THEME) {
        sharedPref.edit().putString("isNightMode", theme.name).apply()
        changeTheme(theme)
    }

    fun getThemeMode(): Constants.THEME {
        val theme = sharedPref.getString("isNightMode", "")
        return if (theme == "") {
            val isDark = checkDarkMode(context)
            if (isDark) Constants.THEME.NIGHT else Constants.THEME.DAY
        } else
            Constants.THEME.valueOf(theme!!)
    }

    fun changeTheme(theme: Constants.THEME) = if (theme == Constants.THEME.NIGHT)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    fun saveUserData(user: User) {
        val userData = Gson().toJson(user)
        sharedPref.edit().putString("USER", userData).apply()
    }

    fun getUserData(): User {
        val userString = sharedPref.getString("USER", null);
        return Gson().fromJson(userString, User::class.java)
    }

    fun removeUserData() {
        sharedPref.edit().remove("USER").apply()
    }
}
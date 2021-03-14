package com.vaibhav.sociofy.ui.settingScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vaibhav.sociofy.data.repository.AuthRepository
import com.vaibhav.sociofy.data.repository.Preferences
import com.vaibhav.sociofy.util.Constants


class SettingsViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences
) : ViewModel() {


    fun setTheme(isChecked: Boolean) = if (isChecked)
        preferences.setThemeMode(Constants.THEME.NIGHT)
    else
        preferences.setThemeMode(Constants.THEME.DAY)

    fun getTheme() = preferences.getThemeMode()

}
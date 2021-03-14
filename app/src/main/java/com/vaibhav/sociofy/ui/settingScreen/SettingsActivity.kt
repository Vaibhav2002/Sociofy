package com.vaibhav.sociofy.ui.settingScreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.sociofy.databinding.ActivitySettingsBinding
import com.vaibhav.sociofy.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theme = viewModel.getTheme()
        binding.darkmodeswitch.isChecked = theme == Constants.THEME.NIGHT
        binding.darkmodeswitch.setOnCheckedChangeListener { _, b ->
            viewModel.setTheme(b)
        }

    }
}
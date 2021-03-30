package com.vaibhav.sociofy.ui.settingScreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var navController: NavController
    private val navArgs by navArgs<SettingsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragmentController)
        viewModel.setUser(navArgs.user)
    }
}
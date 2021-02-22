package com.vaibhav.sociofy.ui.HomeScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.ActivityHomeBinding
import com.vaibhav.sociofy.ui.UploadScreen.UploadActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.d(viewModel.hashCode().toString())
        navController = findNavController(R.id.fragment_host)
        binding.bottomnav.background = null
        binding.bottomnav.setupWithNavController(navController)
        binding.bottomnav.menu.getItem(2).isEnabled = false
        Timber.d(viewModel.hashCode().toString())
        binding.fab.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            overridePendingTransition(R.anim.bottom_enter_anim, R.anim.bottom_exit_anim)
        }
    }
}
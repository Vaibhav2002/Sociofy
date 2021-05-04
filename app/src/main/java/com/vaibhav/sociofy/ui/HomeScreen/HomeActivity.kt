package com.vaibhav.sociofy.ui.HomeScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
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
        navController = findNavController(R.id.fragment_host)
        binding.bottomnav.background = null
        binding.bottomnav.setupWithNavController(navController)
        binding.bottomnav.menu.getItem(2).isEnabled = false
        binding.fab.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            overridePendingTransition(R.anim.bottom_enter_anim, R.anim.bottom_exit_anim)
        }
        setSupportActionBar(binding.myToolbar)
//        binding.hamburger.setOnClickListener {
//            startActivity(Intent(this, SettingsActivity::class.java))
//        }


        val radius = resources.getDimension(R.dimen.corner_radius)
        Timber.d(binding.bottomAppBar.measuredHeight.toString())
        val bottomBarBackground = binding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }


}
package com.vaibhav.sociofy.ui.settingScreen.settingsMainScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentSettingMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingMainFragment : Fragment(R.layout.fragment_setting_main) {
    private lateinit var binding: FragmentSettingMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingMainBinding.bind(view)
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingMainFragment_to_editProfileFragment)
        }
    }

}
package com.vaibhav.sociofy.ui.settingScreen.settingsMainScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentSettingMainBinding
import com.vaibhav.sociofy.ui.authorization.AuthActivity
import com.vaibhav.sociofy.ui.settingScreen.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingMainFragment : Fragment(R.layout.fragment_setting_main) {
    private lateinit var binding: FragmentSettingMainBinding
    private val sharedViewModel: SettingsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingMainBinding.bind(view)
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingMainFragment_to_editProfileFragment)
        }
        binding.savedPosts.setOnClickListener {
            findNavController().navigate(R.id.action_settingMainFragment_to_savedPostsFragment)
        }
        binding.downloadedPosts.setOnClickListener {
            findNavController().navigate(R.id.action_settingMainFragment_to_downloadedPostsFragment)
        }
        binding.logout.setOnClickListener {
            sharedViewModel.logoutUser()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()

        }
    }

}
package com.vaibhav.sociofy.ui.settingScreen.editProfileScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.databinding.FragmentEditProfileBinding
import com.vaibhav.sociofy.ui.settingScreen.SettingsActivity
import com.vaibhav.sociofy.ui.settingScreen.SettingsViewModel
import com.vaibhav.sociofy.util.Constants
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showSuccessToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private val sharedViewModel: SettingsViewModel by activityViewModels()
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        binding.editButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val bio = binding.bioInput.text.toString()
            if (verifyInput(username, bio))
                viewModel.updateUser(imageUri, username, bio)
        }
        (activity as SettingsActivity).supportActionBar?.title = "Edit Profile"
        binding.editImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, Constants.IMAGE_REQUEST_CODE)
        }
        sharedViewModel.userDetail.observe(viewLifecycleOwner, {
            setUserData(it)
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editProfileStatus.collect {
                when (it) {
                    Status.Loading -> {
                        binding.loadingAnim.isVisible = true
                    }
                    Status.Success -> {
                        binding.loadingAnim.isVisible = false
                        showSuccessToast(
                            requireContext(),
                            requireActivity(),
                            "User details updated successfully",
                            "Update success"
                        )
                        findNavController().popBackStack()
                    }
                    is Status.Error -> {
                        binding.loadingAnim.isVisible = false
                        showSuccessToast(
                            requireContext(),
                            requireActivity(),
                            it.error,
                            "Update failed"
                        )
                    }
                }
            }
        }
    }

    private fun setUserData(user: User) {
        binding.apply {
            profileImage.setProfileImage(user.profileImg)
            usernameInput.setText(user.username)
            bioInput.setText(user.bio)
        }
    }

    private fun verifyInput(username: String, bio: String): Boolean {
        val usernameValid = username.isNotBlank() && username.isNotEmpty()
        val bioValid = bio.isNotBlank() && bio.isNotEmpty()
        if (!usernameValid)
            binding.usernameInput.error = "Invalid input"
        if (!bioValid)
            binding.bioInput.error = "Invalid input"
        return usernameValid && bioValid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            imageUri = uri
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.profileImage)
        }
    }
}
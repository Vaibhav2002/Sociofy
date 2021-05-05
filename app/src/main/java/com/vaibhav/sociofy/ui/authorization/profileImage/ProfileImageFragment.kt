package com.vaibhav.sociofy.ui.authorization.profileImage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentProfileImageBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeActivity
import com.vaibhav.sociofy.util.Constants.IMAGE_REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProfileImageFragment : Fragment(R.layout.fragment_profile_image) {
    private lateinit var binding: FragmentProfileImageBinding
    private val viewModel: ProfileImageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileImageBinding.bind(view)
        binding.uploadButton.setOnClickListener {
            viewModel.upload()
        }
        binding.addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uploadStatus.collect { status ->
                when (status) {
                    ProfileImageViewModel.Companion.UploadStatus.Loading -> binding.loadingAnim.isVisible =
                        true
                    ProfileImageViewModel.Companion.UploadStatus.Success -> {
                        binding.loadingAnim.isVisible = false
                        requireActivity().startActivity(
                            Intent(
                                requireActivity(),
                                HomeActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                    is ProfileImageViewModel.Companion.UploadStatus.Error -> {
                        binding.loadingAnim.isVisible = false
                        Timber.d("error ${status.error}")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            viewModel.uri = uri
            Glide.with(requireContext()).load(uri).circleCrop().into(binding.profileImage)
        }
    }

}
package com.vaibhav.sociofy.ui.ProfileScreen

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.databinding.FragmentPostDetailBinding

class PostDetailDialog(private val userId: String, private val postResponse: PostResponse) :
    DialogFragment() {

    private lateinit var binding: FragmentPostDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        val yourDialog = Dialog(requireContext())
        yourDialog.create()
        yourDialog.setContentView(binding.root)
        yourDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.post = postResponse
        Glide.with(this).load(postResponse.profileImg).error(R.drawable.blankuserimg)
            .into(binding.imageView)
        Glide.with(this).load(postResponse.url).error(R.drawable.blankuserimg)
            .into(binding.imageView2)
        setLikedButton()

        binding.likeButton.setOnClickListener {
            likeButtonPressed(postResponse)
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return yourDialog
    }

    private fun setLikedButton() {
        if (postResponse.likedBy.containsKey(userId))
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        else
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
    }

    private fun likeButtonPressed(postResponse: PostResponse) {
        if (postResponse.likedBy.containsKey(userId)) {
            (activity as ProfileActivity).viewModel.dislikeButtonPressed(postResponse)
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
        } else {
            (activity as ProfileActivity).viewModel.likeButtonPressed(postResponse)
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        }
    }

}
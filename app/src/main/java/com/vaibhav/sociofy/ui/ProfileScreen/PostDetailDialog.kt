package com.vaibhav.sociofy.ui.ProfileScreen

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.databinding.FragmentPostDetailBinding

class PostDetailDialog(private val userId: String, private val post: Post) : DialogFragment() {

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
        binding.post = post
        Glide.with(this).load(post.profileImg).error(R.drawable.blankuserimg)
            .into(binding.imageView)
        Glide.with(this).load(post.url).error(R.drawable.blankuserimg).into(binding.imageView2)
        setLikedButton()

        binding.likeButton.setOnClickListener {
            likeButtonPressed(post)
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return yourDialog
    }

    private fun setLikedButton() {
        if (post.likedBy.containsKey(userId))
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        else
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
    }

    private fun likeButtonPressed(post: Post) {
        if (post.likedBy.containsKey(userId)) {
            (activity as ProfileActivity).viewModel.dislikeButtonPressed(post)
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
        } else {
            (activity as ProfileActivity).viewModel.likeButtonPressed(post)
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        }
    }

}
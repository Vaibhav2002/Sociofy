package com.vaibhav.sociofy.ui.HomeScreen.PostDetailDialog

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.databinding.FragmentPostDetailBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeViewModel
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PostDetailFragment : DialogFragment() {

    private val viewModel: PostDetailViewModel by viewModels()
    private val sharedViewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentPostDetailBinding
    private val args by navArgs<PostDetailFragmentArgs>()
    lateinit var post: Post

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPostDetailBinding.inflate(layoutInflater)
        val yourDialog = Dialog(requireContext())
        yourDialog.create()
        yourDialog.setContentView(binding.root)
        yourDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (args.postId == "null") {
            post = args.Post
            binding.post = post
            Glide.with(this).load(post.profileImg).error(R.drawable.blankuserimg)
                .into(binding.imageView)
            Glide.with(this).load(post.url).error(R.drawable.blankuserimg).into(binding.imageView2)
            setLikedButton()
        } else {
            viewModel.getPostFromId(args.postId ?: "")
            viewModel.post.observe(this) {
                post = it
                Glide.with(this).load(it.profileImg).error(R.drawable.blankuserimg)
                    .into(binding.imageView)
                Glide.with(this).load(it.url).error(R.drawable.blankuserimg)
                    .into(binding.imageView2)
                binding.post = it
                setLikedButton()

            }
            lifecycleScope.launchWhenStarted {
                viewModel.postDetailStatus.collect { status ->
                    when (status) {
                        Status.Loading -> binding.loadingAnim.isVisible = true
                        Status.Success -> binding.loadingAnim.isVisible = false
                        is Status.Error -> showViewsForError(status.error)
                    }
                }
            }
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.likeButton.setOnClickListener {
            likeButtonPressed(post = post)
        }
        return yourDialog
    }

    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(requireContext(), requireActivity(), message)
    }

    private fun setLikedButton() {
        if (post.likedBy.containsKey(sharedViewModel.userId))
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        else
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
    }

    private fun likeButtonPressed(post: Post) {
        if (post.likedBy.containsKey(sharedViewModel.userId)) {
            sharedViewModel.dislikeButtonPressed(post)
            binding.likeButton.setImageResource(R.drawable.heart_unselected)
        } else {
            sharedViewModel.likeButtonPressed(post)
            binding.likeButton.setImageResource(R.drawable.heart_filled)
        }
    }


}
package com.vaibhav.sociofy.ui.settingScreen.savedPosts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.databinding.FragmentSavedPostsBinding
import com.vaibhav.sociofy.ui.HomeScreen.feedscreen.FeedAdapter
import com.vaibhav.sociofy.ui.HomeScreen.feedscreen.onItemClick
import com.vaibhav.sociofy.ui.ProfileScreen.ProfileActivity
import com.vaibhav.sociofy.ui.settingScreen.SettingsActivity
import com.vaibhav.sociofy.util.Shared.Resource
import com.vaibhav.sociofy.util.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SavedPostsFragment : Fragment(R.layout.fragment_saved_posts), onItemClick {
    private lateinit var binding: FragmentSavedPostsBinding
    private val viewModel: SavedPostsViewModel by viewModels()
    private lateinit var postAdapter: FeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedPostsBinding.bind(view)
        postAdapter = FeedAdapter(viewModel.userId, this)
        binding.savedPostsRecycler.apply {
            adapter = postAdapter
            setHasFixedSize(false)
        }
        (activity as SettingsActivity).supportActionBar?.title = "Saved Posts"
        viewModel.savedPost.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.loadingAnim.isVisible = false
                    Timber.d(it.message)
                    showErrorToast(
                        requireContext(),
                        requireActivity(),
                        it.message,
                        "Failed to load"
                    )
                }
                is Resource.Loading -> {
                    binding.loadingAnim.isVisible = true
                }
                is Resource.Success -> {
                    binding.loadingAnim.isVisible = false
                    postAdapter.submitList(it.data ?: emptyList())
                }
            }
        }


    }

    override fun onProfileClick(post: Post) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        val user = User(post.uid, post.username, "", post.profileImg)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    override fun onLikeClicked(post: Post) {
        if (post.likedBy.containsKey(viewModel.userId))
            viewModel.dislikeButtonPressed(post)
        else
            viewModel.likeButtonPressed(post)
    }

    override fun onShareClicked(post: Post) {

    }

    override fun onSaveClicked(post: Post) {

    }

    override fun onDownloadClicked(post: Post) {

    }
}
package com.vaibhav.sociofy.ui.HomeScreen.feedscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.databinding.FragmentFeedBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeViewModel
import com.vaibhav.sociofy.ui.ProfileScreen.ProfileActivity
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : onItemClick, Fragment(R.layout.fragment_feed) {

    private lateinit var binding: FragmentFeedBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)
        setHasOptionsMenu(false)
        val feedAdapter = FeedAdapter(userId = viewModel.userId, this)
        binding.postRecycle.apply {
            adapter = feedAdapter
            setHasFixedSize(false)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.feedStatus.collect { status ->
                when (status) {
                    Status.Loading -> showViewsForLoading()
                    Status.Success -> showViewsForSuccess()
                    is Status.Error -> showViewsForError(status.error)
                }

            }
        }
        viewModel.feed.observe(viewLifecycleOwner, {
            feedAdapter.submitList(it)
        })

    }


    override fun onProfileClick(post: Post) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        val user = User(post.uid, post.username, "", post.profileImg)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun showViewsForSuccess() {
        binding.apply {
            loadingAnim.isVisible = false
            postRecycle.isVisible = true
        }
    }

    override fun onLikeClicked(post: Post) {
        if (post.likedBy.containsKey(viewModel.userId))
            viewModel.dislikeButtonPressed(post)
        else
            viewModel.likeButtonPressed(post)
    }

    override fun onShareClicked(post: Post) {
        Timber.d("share clicked")
    }

    private fun showViewsForLoading() {
        binding.apply {
            loadingAnim.isVisible = true
            postRecycle.isVisible = false
        }
    }


    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(requireContext(), requireActivity(), message)
    }
}
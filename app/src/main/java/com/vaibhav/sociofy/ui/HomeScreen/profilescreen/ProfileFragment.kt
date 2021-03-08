package com.vaibhav.sociofy.ui.HomeScreen.profilescreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.databinding.FragmentProfileBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeViewModel
import com.vaibhav.sociofy.ui.UserListScreen.UserListActivity
import com.vaibhav.sociofy.util.Constants
import com.vaibhav.sociofy.util.Shared.GridPostsAdapter
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showErrorToast
import kotlinx.coroutines.flow.collect
import timber.log.Timber


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        Timber.d(viewModel.hashCode().toString())

        val postAdapter = GridPostsAdapter(onImageClick = {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToPostDetailFragment(
                    Post = it,
                    postId = "null"
                )
            findNavController().navigate(action)


        })

        binding.followerCount.setOnClickListener {
            navigateToUserList(viewModel.userDetails.value ?: User(), Constants.LIST_FOR.Followers)
        }
        binding.followingCount.setOnClickListener {
            navigateToUserList(viewModel.userDetails.value ?: User(), Constants.LIST_FOR.Following)
        }


        binding.postRecycler.apply {
            adapter = postAdapter
            setHasFixedSize(false)
        }
        viewModel.userDetails.observe(viewLifecycleOwner, { user ->
            binding.user = user
            Glide.with(this).load(user.profileImg).error(R.drawable.blankuserimg)
                .into(binding.circularImageView)
        })
        viewModel.userPosts.observe(viewLifecycleOwner, { posts ->
            binding.postsCount.text = posts.size.toString()
            postAdapter.submitList(posts)
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userPageStatus.collect { status ->
                when (status) {
                    Status.Loading -> binding.loadingAnim.isVisible =
                        true
                    Status.Success -> binding.loadingAnim.isVisible =
                        false
                    is Status.Error -> showViewsForError(status.error)
                }
            }
        }
    }

    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(requireContext(), requireActivity(), message)
    }

    private fun navigateToUserList(user: User, listFor: Constants.LIST_FOR) {
        val intent = Intent(requireContext(), UserListActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("type", listFor)
        startActivity(intent)
    }
}
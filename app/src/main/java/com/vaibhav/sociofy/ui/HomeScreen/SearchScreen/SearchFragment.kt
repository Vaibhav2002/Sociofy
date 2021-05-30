package com.vaibhav.sociofy.ui.HomeScreen.SearchScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.remote.User
import com.vaibhav.sociofy.databinding.FragmentSearchBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeViewModel
import com.vaibhav.sociofy.ui.ProfileScreen.ProfileActivity
import com.vaibhav.sociofy.util.Shared.PostGridAdapterSmall
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.Shared.UserListHorizontalAdapter
import com.vaibhav.sociofy.util.showErrorToast
import kotlinx.coroutines.flow.collect


class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        setHasOptionsMenu(false)
        val onUserClicked = { user: User ->
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        val userAdapter = UserListHorizontalAdapter(onUserClicked)
        val postAdapter = PostGridAdapterSmall {
            val action =
                SearchFragmentDirections.actionSearchFragmentToPostDetailFragment(
                    Post = it,
                    postId = "null"
                )
            findNavController().navigate(action)
        }

        binding.usersRecycle.apply {
            adapter = userAdapter
            setHasFixedSize(false)
        }
        binding.postsRecycleGrid.apply {
            adapter = postAdapter
            setHasFixedSize(false)
        }
        viewModel.allFeed.observe(viewLifecycleOwner, {
            if (it.isEmpty())
                showEmptyState()
            else
                postAdapter.submitList(it)
        })

        viewModel.userList.observe(viewLifecycleOwner, {
            userAdapter.submitList(it)
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchScreenStatus.collect { status ->
                when (status) {
                    Status.Loading -> binding.loadingAnim.isVisible = true
                    Status.Success -> binding.loadingAnim.isVisible = false
                    is Status.Error -> showViewsForError(status.error)
                }
            }
        }


    }

    private fun showViewsForError(message: String) {
        binding.loadingAnim.isVisible = false
        showErrorToast(requireContext(), requireActivity(), message)
    }

    private fun showEmptyState() {
        binding.loadingAnim.isVisible = false
        binding.emptyStateTextView.text =
            resources.getString(R.string.empty_state_search)
        binding.emptyStateLinear.isVisible = true
    }

}
package com.vaibhav.sociofy.ui.HomeScreen.NotificationScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.data.models.User
import com.vaibhav.sociofy.databinding.FragmentNotificationsBinding
import com.vaibhav.sociofy.ui.HomeScreen.HomeViewModel
import com.vaibhav.sociofy.ui.ProfileScreen.ProfileActivity
import com.vaibhav.sociofy.util.Shared.Status
import com.vaibhav.sociofy.util.showErrorToast
import kotlinx.coroutines.flow.collect

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private lateinit var binding: FragmentNotificationsBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)
        setHasOptionsMenu(false)
        notificationAdapter = NotificationAdapter({
            val action =
                NotificationsFragmentDirections.actionNotificationsFragmentToPostDetailFragment(
                    Post = Post(),
                    postId = it
                )
            findNavController().navigate(action)
        }, {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            val user = User(it.uid, it.username, "", it.profilePic)
            intent.putExtra("user", user)
            startActivity(intent)
        })
        binding.notifyRecycler.apply {
            adapter = notificationAdapter
            setHasFixedSize(false)
        }
        viewModel.notificationList.observe(viewLifecycleOwner, {
            notificationAdapter.submitList(it)
        })
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notificationStatus.collect { status ->
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

}
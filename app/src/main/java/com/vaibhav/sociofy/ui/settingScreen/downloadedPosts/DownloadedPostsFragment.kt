package com.vaibhav.sociofy.ui.settingScreen.downloadedPosts

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentDownloadedPostsBinding
import com.vaibhav.sociofy.ui.settingScreen.SettingsActivity
import com.vaibhav.sociofy.util.Shared.showDeleteDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadedPostsFragment : Fragment(R.layout.fragment_downloaded_posts) {

    private lateinit var binding: FragmentDownloadedPostsBinding
    private val viewModel: DownloadedPostViewModel by viewModels()
    private val downloadedPostAdapter = DownloadedPostAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadedPostsBinding.bind(view)
        binding.downloadedRecycler.apply {
            adapter = downloadedPostAdapter
            setHasFixedSize(false)
        }

        viewModel.downloadedPosts.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                showEmptyState()
            else
                downloadedPostAdapter.submitList(it)
        }
        (activity as SettingsActivity).supportActionBar?.title = "Downloaded Posts"

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val downloadedPost = downloadedPostAdapter.currentList[viewHolder.adapterPosition]
                requireContext().showDeleteDialog(
                    "Delete Saved Post",
                    "Are you sure that you want to delete this saved post"
                ) {
                    viewModel.deleteDownloadedPost(downloadedPost)
                }
            }
        }).attachToRecyclerView(binding.downloadedRecycler)
    }


    private fun showEmptyState() {
        binding.emptyStateTextView.text = resources.getString(R.string.empty_state_download)
        binding.emptyStateLinear.isVisible = true
    }
}


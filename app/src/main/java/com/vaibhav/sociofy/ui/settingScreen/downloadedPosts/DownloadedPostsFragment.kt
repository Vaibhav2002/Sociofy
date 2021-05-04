package com.vaibhav.sociofy.ui.settingScreen.downloadedPosts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.databinding.FragmentDownloadedPostsBinding
import com.vaibhav.sociofy.ui.settingScreen.SettingsActivity
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
            downloadedPostAdapter.submitList(it)
        }
        (activity as SettingsActivity).supportActionBar?.title = "Downloaded Posts"
    }

}
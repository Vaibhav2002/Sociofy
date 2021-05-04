package com.vaibhav.sociofy.ui.settingScreen.downloadedPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.data.models.local.DownloadedPost
import com.vaibhav.sociofy.databinding.DownloadedPostItemBinding

class DownloadedPostAdapter :
    ListAdapter<DownloadedPost, DownloadedPostAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            DownloadedPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: DownloadedPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(data: DownloadedPost) {
            binding.downloadedPost = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<DownloadedPost>() {
        override fun areItemsTheSame(
            oldItem: DownloadedPost,
            newItem: DownloadedPost
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DownloadedPost,
            newItem: DownloadedPost
        ): Boolean {
            return oldItem.likes == newItem.likes
        }
    }

}

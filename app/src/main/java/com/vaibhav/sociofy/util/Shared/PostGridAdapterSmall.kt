package com.vaibhav.sociofy.util.Shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.databinding.PostGridItemSmallBinding

class PostGridAdapterSmall(private val onImageClick: (PostResponse) -> Unit) :
    ListAdapter<PostResponse, PostGridAdapterSmall.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = PostGridItemSmallBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: PostGridItemSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onImageClick.invoke(currentList[adapterPosition])
            }
        }

        fun bind(data: PostResponse) {
            binding.post = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

}
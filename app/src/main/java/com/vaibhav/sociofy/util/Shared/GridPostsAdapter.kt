package com.vaibhav.sociofy.util.Shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.data.models.Post
import com.vaibhav.sociofy.databinding.ProfileScreenPostGridItemBinding

class GridPostsAdapter(private val onImageClick: (Post) -> Unit) :
    ListAdapter<Post, GridPostsAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ProfileScreenPostGridItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: ProfileScreenPostGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onImageClick.invoke(currentList[adapterPosition])
            }
        }

        fun bind(data: Post) {
            binding.post = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem == newItem
        }
    }

}
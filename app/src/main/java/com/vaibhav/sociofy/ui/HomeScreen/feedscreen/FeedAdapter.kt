package com.vaibhav.sociofy.ui.HomeScreen.feedscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.R
import com.vaibhav.sociofy.data.models.remote.PostResponse
import com.vaibhav.sociofy.databinding.PostItemBinding
import timber.log.Timber

class FeedAdapter(private val userId: String, private val onItemClick: onItemClick) :
    ListAdapter<PostResponse, FeedAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageView.setOnClickListener {
                onItemClick.onProfileClick(currentList[adapterPosition])
            }
            binding.likeButton.setOnClickListener {
                onItemClick.onLikeClicked(currentList[adapterPosition])
            }
            binding.shareButton.setOnClickListener {
                onItemClick.onShareClicked(currentList[adapterPosition])
            }
            val popupMenu = PopupMenu(binding.root.context, binding.optionsMenu)
                .apply {
                    inflate(R.menu.feed_post_options_menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.savePost -> {
                                onItemClick.onSaveClicked(currentList[adapterPosition])
                                true
                            }
                            else -> {
                                onItemClick.onDownloadClicked(currentList[adapterPosition])
                                true
                            }
                        }
                    }
                }

            binding.optionsMenu.setOnClickListener {
                popupMenu.show()
            }

        }

        fun bind(data: PostResponse) {
            if (data.likedBy.containsKey(userId))
                binding.likeButton.setImageResource(R.drawable.heart_filled)
            else
                binding.likeButton.setImageResource(R.drawable.heart_unselected)
            binding.post = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            return oldItem.postUid == newItem.postUid
        }

        override fun areContentsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            Timber.d("${oldItem.likes} ${newItem.likes}  ${oldItem.likes == newItem.likes}")
            return oldItem.likes == newItem.likes
        }
    }

}

interface onItemClick {
    fun onProfileClick(postResponse: PostResponse)
    fun onLikeClicked(postResponse: PostResponse)
    fun onShareClicked(postResponse: PostResponse)
    fun onSaveClicked(postResponse: PostResponse)
    fun onDownloadClicked(postResponse: PostResponse)
}
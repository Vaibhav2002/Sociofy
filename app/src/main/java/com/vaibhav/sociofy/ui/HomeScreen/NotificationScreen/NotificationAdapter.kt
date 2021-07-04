package com.vaibhav.sociofy.ui.HomeScreen.NotificationScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.sociofy.data.models.Notification
import com.vaibhav.sociofy.databinding.NotificationItemBinding

class NotificationAdapter(
    private val onImageClick: (String) -> Unit,
    private val onUserClick: (Notification) -> Unit
) :
    ListAdapter<Notification, NotificationAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageView6.setOnClickListener {
                onImageClick.invoke(currentList[adapterPosition].postId)
            }
            binding.circularImageView2.setOnClickListener {
                onUserClick.invoke(currentList[adapterPosition])
            }
        }

        fun bind(data: Notification) {
            binding.notification = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return oldItem == newItem
        }
    }

}



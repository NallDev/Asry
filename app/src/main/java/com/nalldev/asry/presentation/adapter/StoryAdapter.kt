package com.nalldev.asry.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nalldev.asry.databinding.ViewStoryBinding
import com.nalldev.asry.domain.models.StoryModel

class StoryAdapter(val onItemClick: ((StoryModel) -> Unit)) : PagingDataAdapter<StoryModel, StoryAdapter.ViewHolder>(this) {

    inner class ViewHolder(private val binding: ViewStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyData : StoryModel) {
            binding.tvItemName.text = storyData.name
            binding.tvItemDescription.text = storyData.description
            binding.ivItemPhoto.load(storyData.photoUrl) {
                crossfade(true)
            }

            binding.root.setOnClickListener {
                onItemClick.invoke(storyData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(holder.bindingAdapterPosition)?.let {
            holder.bind(it) }
    }

    companion object : DiffUtil.ItemCallback<StoryModel>() {
        override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean =
            oldItem == newItem
    }
}
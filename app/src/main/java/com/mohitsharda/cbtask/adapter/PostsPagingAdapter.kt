package com.mohitsharda.cbtask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohitsharda.cbtask.databinding.PostViewBinding
import com.mohitsharda.cbtask.model.PostModel
import javax.inject.Inject

class PostsPagingAdapter @Inject constructor() :
    PagingDataAdapter<PostModel, PostsPagingAdapter.PostViewHolder>(DiffUtils) {

    class PostViewHolder(private val binding: PostViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postModel: PostModel) {
            binding.apply {
                tvId.text = "Id : ${postModel.id}"
                tvUserId.text = "UserId : ${postModel.userId}"
                tvTitle.text = "Title : ${postModel.title}"
                tvBody.text = "Body : ${postModel.body}"
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        if (post != null) {
            holder.bind(post)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    object DiffUtils : DiffUtil.ItemCallback<PostModel>() {

        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem == newItem
        }

    }
}
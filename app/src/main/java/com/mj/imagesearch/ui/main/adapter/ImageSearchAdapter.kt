package com.mj.imagesearch.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseViewHolder
import com.mj.imagesearch.databinding.ImageSearchItemBinding

class ImageSearchAdapter(
    private val like: (ThumbnailData) -> Unit
) : PagingDataAdapter<ThumbnailData, ImageSearchAdapter.SearchViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(viewBind(parent, R.layout.image_search_item))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private fun <T : ViewDataBinding> viewBind(parent: ViewGroup, layoutRes: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }

    inner class SearchViewHolder(private val binding: ImageSearchItemBinding) :
        BaseViewHolder<ThumbnailData>(binding.root) {
        override fun bind(item: ThumbnailData) {

            binding.data = item

//            Glide.with(binding.root)
//                .load(item.thumbnail)
//                .error(R.drawable.ic_baseline_browser_not_supported_24)
//                .centerCrop()
//                .into(binding.imageView)
//
            binding.imageView.setOnClickListener {
                like.invoke(item)
            }
        }
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<ThumbnailData>() {
            override fun areItemsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData): Boolean {
                return oldItem.thumbnail == newItem.thumbnail
            }

            override fun areContentsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData): Boolean {
                return oldItem == newItem
            }
        }
    }
}
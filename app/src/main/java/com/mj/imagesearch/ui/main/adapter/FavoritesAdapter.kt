package com.mj.imagesearch.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseViewHolder
import com.mj.imagesearch.databinding.ImageSearchItemBinding

class FavoritesAdapter(
    private val unLike: (ThumbnailData) -> Unit
) : ListAdapter<ThumbnailData, FavoritesAdapter.FavoriteViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(viewBind(parent, R.layout.image_search_item))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private fun <T : ViewDataBinding> viewBind(parent: ViewGroup, layoutRes: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }

    inner class FavoriteViewHolder(private val binding: ImageSearchItemBinding) :
        BaseViewHolder<ThumbnailData>(binding.root) {
        override fun bind(item: ThumbnailData) {
            binding.data = item

            binding.imageView.setOnClickListener {
                //리스트에서 삭제
                unLike.invoke(item)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<ThumbnailData>() {
        override fun areItemsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData) = oldItem.thumbnail == newItem.thumbnail
        override fun areContentsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData) = oldItem == newItem
    }
}
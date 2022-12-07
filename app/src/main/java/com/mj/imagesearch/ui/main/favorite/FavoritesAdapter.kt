package com.mj.imagesearch.ui.main.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R
import com.mj.imagesearch.databinding.RowFavoriteImageItemBinding
import com.mj.imagesearch.ui.main.favorite.FavoritesAdapter.*
import timber.log.Timber

class FavoritesAdapter(private val callback: FavoriteCallback) :
    ListAdapter<Item, FavoriteViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.uid == newItem.uid
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }

    interface FavoriteCallback {
        fun toggle(data: Item)
    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("favoriteImageItems", "callback")
        fun RecyclerView.setItems(items: List<Item>?, callback: FavoriteCallback) {
            Timber.d("in adapter : $items")
            val adapter = adapter as? FavoritesAdapter
                ?: FavoritesAdapter(callback).also { adapter = it }
            adapter.submitList(items ?: return)
        }
    }

    class FavoriteViewHolder(itemView: View, val update: (item: Item) -> Unit) : ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder = parent.run {
        val binding = DataBindingUtil.inflate<RowFavoriteImageItemBinding>(
            LayoutInflater.from(parent.context), R.layout.row_favorite_image_item, this, false
        ).apply { lifecycleOwner = findViewTreeLifecycleOwner() }
        return FavoriteViewHolder(binding.root) u@{ item ->
            binding.item = item
            binding.callback = callback
        }
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.update(getItem(position) ?: return)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    data class Item(
        val uid: Long,
        val thumbnail: String
    )
}
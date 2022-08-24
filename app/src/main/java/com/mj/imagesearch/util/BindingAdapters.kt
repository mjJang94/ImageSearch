package com.mj.imagesearch.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("thumbnailUrl")
    fun AppCompatImageView.setListThumbnailUrl(data: ThumbnailData?) {
        if (data == null){
            setImageResource(R.drawable.ic_baseline_browser_not_supported_24)
        }else{
            Glide.with(context)
                .load(data.thumbnail)
                .error(R.drawable.ic_baseline_browser_not_supported_24)
                .centerCrop()
                .into(this)
        }
    }
}
package com.mj.imagesearch.util

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("thumbnailUrl")
    fun AppCompatImageView.setListThumbnailUrl(url: String?) {
        if (url == null) {
            setImageResource(R.drawable.ic_baseline_browser_not_supported_24)
        } else {
            Glide.with(context)
                .load(url)
                .error(R.drawable.ic_baseline_browser_not_supported_24)
                .centerCrop()
                /**
                 * 특이한 경우가 아닌 경우에는 캐싱을 위해 false 지정
                 */
                .skipMemoryCache(false)
                /**
                 * 썸네일 수준의 이미지 이므로 기본 포맷인 ARGB_8888을 RGB_565로 변경하여 색상 품질을 낮춰 메모리 효율을 올린다 (약 50% 상승 기대)
                 */
                .format(DecodeFormat.PREFER_RGB_565)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun View.setVisibility(visible: Boolean?) {
        if (visible == null) return

        val visibility = when (visible) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        this.visibility = visibility
    }

    @JvmStatic
    @BindingAdapter("searchAction")
    fun EditText.setSearchActionEvent(event : () -> Unit?){

        setOnEditorActionListener { _, id, _ ->
            var handled = false

            if (id == EditorInfo.IME_ACTION_SEARCH){
                event.invoke()
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }
}
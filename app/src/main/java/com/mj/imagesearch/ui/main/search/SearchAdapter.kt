package com.mj.imagesearch.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseViewHolder
import com.mj.imagesearch.databinding.RowSearchImageItemBinding

class SearchAdapter(
    private val like: (ThumbnailData) -> Unit
) : PagingDataAdapter<ThumbnailData, SearchAdapter.SearchViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(viewBind(parent, R.layout.row_search_image_item))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 4).apply {
                /**
                 * UI Thread 에서 View 의 inflate & bind 작업이 완료되면 순차적으로 GPU Render Thread 에서 렌더링 작업을 하는데, 이때 UI Thread 는 유휴 상태가 된다.
                 * 리사이클러뷰의 스크롤이 일어날 때 새로운 View 를 노출하기 위해 위 순서(inflate & bind)를 반복하게 되는데
                 * 문제는 위 작업의 비용이 크다 보니 Render Thread 에서 작업이 끝나고 사용자에게 노출되는 순간과 겹칠때가 있다.
                 *
                 * 그래서 inflate & bind 작업과 동시에 렌더링 된 UI 가 표시됨으로 순간적인 버벅거림이 발생하는 케이스가 있다.
                 *
                 * 때문에 prefetch 방식을 이용하여 스크롤 할때 inflate 가 필요한 경우 미리미리 bind 해놓아야 하므로
                 * @setItemPrefetchEnabled() 는 기본적으로 true 이다 (굳이 명시해서 쓸 필욘 없다..).
                 *
                 * 하지만 스크롤이 조금만 발생하도록 의도된 UX 라던가, 전체 아이템의 개수가 한정적이거나 매우 적을 경우엔 prefetch 방식이 오히려 비용이 들어가는 문제가 될 수 있으므로
                 * false 로 하는 것이 좋고, 반대로 스크롤이 많을 경우엔 true 가 유리하다.
                 */
                isItemPrefetchEnabled = true
            }
            itemAnimator = null
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

    inner class SearchViewHolder(private val binding: RowSearchImageItemBinding) :
        BaseViewHolder<ThumbnailData>(binding.root) {
        override fun bind(item: ThumbnailData) {
            binding.item = item
            binding.imageView.setOnClickListener {
                like.invoke(item)
            }
        }
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<ThumbnailData>() {
            override fun areItemsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData) = oldItem.thumbnail == newItem.thumbnail
            override fun areContentsTheSame(oldItem: ThumbnailData, newItem: ThumbnailData) = oldItem == newItem
        }
    }
}
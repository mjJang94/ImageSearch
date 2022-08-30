package com.mj.imagesearch.ui.main

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseFragment
import com.mj.imagesearch.databinding.FragmentSearchBinding
import com.mj.imagesearch.ui.main.CommonSearchViewModel.*
import com.mj.imagesearch.ui.main.CommonSearchViewModel.ToggleType.LIKE
import com.mj.imagesearch.ui.main.adapter.ImageSearchAdapter
import com.mj.imagesearch.util.hideKeyboard
import com.mj.imagesearch.util.setGone
import com.mj.imagesearch.util.setVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


class ImageSearchFragment : BaseFragment<FragmentSearchBinding, CommonSearchViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_search

    override val viewModel: CommonSearchViewModel by activityViewModels()

    private val adapter: ImageSearchAdapter = ImageSearchAdapter {
        viewModel.toggle(LIKE, it)
    }

    override fun initOnCreateView() {
        initView()

        /**
         * lifecycleScope - fragment 의 lifecycle 을 따름
         * viewLifecycleScope.lifecycleScope - fragment owner 의 라이프 사이클을 따름
         */
        launch {
            viewModel.pagingDataFlow
                .collectLatest { items ->
                    adapter.submitData(items)
                }
        }

        launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                Timber.d("loadStateFlow : ${loadState.refresh}")
                viewModel.setUIState(loadState.refresh)
            }
        }

        /**
         * 홈 버튼을 눌러서 화면이 Background 에 진입했을 때, 변경된 데이터에 의해 화면을 업데이트 해줄 필요가 없으므로
         * UI가 사용자에게 보여지고 있지 않을땐 데이터를 observe 하지 않도록 repeatOnLifecycle() 사용.
         * (Lifecycle 상황에 맞게 알아서 collect / cancel 을 반복함.)
         */
        repeatOnOwnerStarted {
            viewModel.eventFlow.collect { event ->
                Timber.e("repeatOnOwnerStarted $event")
                handleUIEvent(event)
            }
        }
    }

    private fun initView() = with(binding) {
        recyclerView.adapter = adapter
        search.setOnClickListener {
            val query = binding.editText.text.trim().toString()
            viewModel.handleQuery(query)
        }
    }

    private fun handleUIEvent(event: SearchUIEvent) {
        when (event) {
            is SearchUIEvent.Loading -> {
                onLoad()
            }

            is SearchUIEvent.Error -> {
                onError(event.msg)
            }

            is SearchUIEvent.NotLoading -> {
                onSuccess()
            }

        }
    }

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
    }

    private fun onError(msg: String) = with(binding) {
        progressBar.setGone()
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() = with(binding) {
        progressBar.run {
            setGone()
            hideKeyboard()
        }
    }
}
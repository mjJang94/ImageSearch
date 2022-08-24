package com.mj.imagesearch.ui.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseFragment
import com.mj.imagesearch.databinding.FragmentFavoriteBinding
import com.mj.imagesearch.ui.main.CommonSearchViewModel.ToggleType.*
import com.mj.imagesearch.ui.main.adapter.FavoritesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, CommonSearchViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_favorite

    override val viewModel: CommonSearchViewModel by activityViewModels()

    private val adapter: FavoritesAdapter = FavoritesAdapter {
        viewModel.toggle(UNLIKE, it)
    }

    override fun initOnCreateView() {

        initRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoritesFlow.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun initRecyclerView() = with(binding) {
        rcyFavorite.adapter = adapter
        rcyFavorite.layoutManager = GridLayoutManager(context, 4)
    }
}
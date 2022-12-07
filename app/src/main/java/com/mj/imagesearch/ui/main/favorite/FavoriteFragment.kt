package com.mj.imagesearch.ui.main.favorite

import androidx.fragment.app.viewModels
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseFragment
import com.mj.imagesearch.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_favorite

    override val viewModel: FavoriteViewModel by viewModels()

    override fun initOnCreateView() {
        binding.vm = viewModel
    }
}
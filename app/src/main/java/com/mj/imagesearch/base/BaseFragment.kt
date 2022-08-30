package com.mj.imagesearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, R : ViewModel> : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = lifecycleScope.coroutineContext

    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: R
    abstract fun initOnCreateView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        initOnCreateView()
        return binding.root
    }

    fun repeatOnOwnerStarted(block: suspend CoroutineScope.() -> Unit) {
        launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }
}
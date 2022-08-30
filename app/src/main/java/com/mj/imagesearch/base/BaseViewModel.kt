package com.mj.imagesearch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class BaseViewModel<T> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    private val _eventFlow = MutableSharedFlow<T>()
    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * Test 를 위한 LiveData..
     */
    private val _testState = MutableLiveData<T?>()
    val testState = _testState

    fun triggerEvent(event: T) {
        viewModelScope.launch {
            _eventFlow.emit(event)
            _testState.postValue(event)
        }
    }
}
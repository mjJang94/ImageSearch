package com.mj.imagesearch.ui.main

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.HandleFavoriteImageSourceUseCase
import com.mj.domain.usecase.HandleSearchImageSourceUseCase
import com.mj.imagesearch.data.NaverImageSearchDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*


@HiltViewModel
class CommonSearchViewModel @Inject constructor(
    private val handleFavoriteImageSourceUseCase: HandleFavoriteImageSourceUseCase,
    private val handleSearchImageSourceUseCase: HandleSearchImageSourceUseCase
) : ViewModel() {
    private val queryFlow = MutableSharedFlow<String>()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    /**
     * Test 를 위한 LiveData..
     */
    private val _testState = MutableLiveData<UIEvent>()
    val testState = _testState

    /**
     * flatMapLatest - 검색도중 유저가 다른 검색어로 검색을 시도한 경우 다른 검색어 적용을 위해 선택
     */
    val pagingDataFlow = queryFlow
        .flatMapLatest {
            searchImages(it)
        }
        .cachedIn(viewModelScope)

    val favoritesFlow = handleFavoriteImageSourceUseCase.favoriteImages.asFlow()

    private fun searchImages(query: String): Flow<PagingData<ThumbnailData>> =
        loadRemoteSearchImages(query)

    fun handleQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    fun toggle(type: ToggleType, item: ThumbnailData) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                ToggleType.LIKE -> {
                    saveFavoriteImage(item)
                }

                ToggleType.UNLIKE -> {
                    deleteFavoriteImage(item)
                }
            }
        }
    }

    fun setUIState(state: LoadState) {
        when (state) {
            is LoadState.Loading -> {
                triggerEvent(UIEvent.Loading)
            }
            is LoadState.Error -> {
                triggerEvent(UIEvent.Error(state.error.message ?: "Network Request failed!"))
            }
            else -> {
                triggerEvent(UIEvent.NotLoading)
            }
        }
    }

    private fun loadRemoteSearchImages(query: String): Flow<PagingData<ThumbnailData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NaverImageSearchDataSource.defaultDisplay,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverImageSearchDataSource(query, handleSearchImageSourceUseCase)
            }
        ).flow
    }

    private suspend fun saveFavoriteImage(data: ThumbnailData) {
        handleFavoriteImageSourceUseCase.insert(data)
        Timber.d("save thumbnail : ${data}")
    }

    private suspend fun deleteFavoriteImage(data: ThumbnailData) {
        handleFavoriteImageSourceUseCase.delete(data)
        Timber.d("delete thumbnail : ${data}")
    }

    private fun triggerEvent(event: UIEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
            _testState.postValue(event)
        }
    }

    sealed class UIEvent {
        data class Error(val msg: String) : UIEvent()
        object Loading : UIEvent()
        object NotLoading : UIEvent()
    }

    enum class ToggleType {
        LIKE, UNLIKE
    }
}
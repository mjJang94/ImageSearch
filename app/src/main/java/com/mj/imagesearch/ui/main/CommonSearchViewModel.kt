package com.mj.imagesearch.ui.main

import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.domain.usecase.GetRemoteImageUseCase
import com.mj.imagesearch.base.BaseViewModel
import com.mj.imagesearch.data.NaverImageSearchDataSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber


@HiltViewModel
class CommonSearchViewModel @Inject constructor(
    private val getLocalImageUseCase: GetLocalImageUseCase,
    private val getRemoteImageUseCase: GetRemoteImageUseCase
) : BaseViewModel<CommonSearchViewModel.SearchUIEvent>(){

    private val queryFlow = MutableSharedFlow<String>()
    /**
     * flatMapLatest - 검색도중 유저가 다른 검색어로 검색을 시도한 경우 다른 검색어 적용을 위해 선택
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow = queryFlow
        .flatMapLatest {
            searchImages(it)
        }.flowOn(Dispatchers.Default).cachedIn(this)

    val favoritesFlow = getLocalImageUseCase.favoriteImages

    private fun searchImages(query: String): Flow<PagingData<ThumbnailData>> =
        loadRemoteSearchImages(query)

    fun handleQuery(query: String) {
        launch {
            queryFlow.emit(query)
        }
    }

    fun toggle(type: ToggleType, item: ThumbnailData) {
        launch(Dispatchers.IO) {
            when (type) {
                ToggleType.LIKE -> {
                    saveFavoriteImage(item)
                }

                ToggleType.DISLIKE -> {
                    deleteFavoriteImage(item)
                }
            }
        }
    }

    fun setUIState(state: LoadState) {
        when (state) {
            is LoadState.Loading -> {
                triggerEvent(SearchUIEvent.Loading)
            }
            is LoadState.Error -> {
                triggerEvent(SearchUIEvent.Error(state.error.message ?: "Network Request failed!"))
            }
            else -> {
                triggerEvent(SearchUIEvent.NotLoading)
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
                NaverImageSearchDataSource(query, getRemoteImageUseCase)
            }
        ).flow
    }

    private suspend fun saveFavoriteImage(data: ThumbnailData) {
        getLocalImageUseCase.insert(data)
        Timber.d("save thumbnail : $data")
    }

    private suspend fun deleteFavoriteImage(data: ThumbnailData) {
        getLocalImageUseCase.delete(data)
        Timber.d("delete thumbnail : $data")
    }

    sealed class SearchUIEvent {
        data class Error(val msg: String) : SearchUIEvent()
        object Loading : SearchUIEvent()
        object NotLoading : SearchUIEvent()
    }

    enum class ToggleType {
        LIKE, DISLIKE
    }
}
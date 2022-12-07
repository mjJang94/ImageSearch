package com.mj.imagesearch.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.domain.usecase.GetRemoteImageUseCase
import com.mj.imagesearch.data.NaverImageSearchDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRemoteImageUseCase: GetRemoteImageUseCase,
    private val getLocalImageUseCase: GetLocalImageUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val queryFlow = MutableSharedFlow<String>()

    private val _eventFlow = MutableSharedFlow<SearchUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    val eventStateForTest = _eventFlow.asLiveData()

    /**
     * flatMapLatest - 검색도중 유저가 다른 검색어로 검색을 시도한 경우 다른 검색어 적용을 위해 선택
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow = queryFlow
        .flatMapLatest {
            searchImages(it)
        }.flowOn(Dispatchers.Default).cachedIn(this)

    val keyword = MutableLiveData<String>()

    fun search() {
        val query = keyword.value?.trim() ?: return
        launch(Dispatchers.Default) {
            handleQuery(query)
        }
    }

    fun toggle(item: ThumbnailData) {
        launch(Dispatchers.IO) {
            saveFavoriteImage(item)
        }
    }

    private fun searchImages(query: String): Flow<PagingData<ThumbnailData>> =
        loadRemoteSearchImages(query)

    private fun handleQuery(query: String) {
        launch {
            queryFlow.emit(query)
        }
    }

    suspend fun setUIState(state: LoadState) {
        when (state) {
            is LoadState.Loading -> {
                _eventFlow.emit(SearchUIEvent.Loading)
            }
            is LoadState.Error -> {
                _eventFlow.emit(SearchUIEvent.Error(state.error.message ?: "Network Request failed!"))
            }
            else -> {
                _eventFlow.emit(SearchUIEvent.NotLoading)
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

    sealed class SearchUIEvent {
        data class Error(val msg: String) : SearchUIEvent()
        object Loading : SearchUIEvent()
        object NotLoading : SearchUIEvent()
    }
}

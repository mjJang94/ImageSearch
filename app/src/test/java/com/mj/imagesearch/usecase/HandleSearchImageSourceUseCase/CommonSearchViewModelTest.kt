package com.mj.imagesearch.usecase.HandleSearchImageSourceUseCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import com.mj.domain.repository.ImageRepository
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.domain.usecase.GetRemoteImageUseCase
import com.mj.imagesearch.ui.main.MainViewModel
import com.mj.imagesearch.ui.main.MainViewModel.*
import com.mj.imagesearch.ui.main.search.SearchViewModel
import com.mj.imagesearch.ui.main.search.SearchViewModel.*
import com.mj.imagesearch.util.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import timber.log.Timber

@ExperimentalCoroutinesApi
class CommonSearchViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUIStates = mutableListOf<SearchUIEvent>()

    private val fakeRepo = object : ImageRepository {

        override val favoriteImages: LiveData<List<ThumbnailData>>
            get() = MutableLiveData(emptyList())

        override suspend fun getFavoriteImages(): List<ThumbnailData> {
            Timber.d("mock getFavoriteImages()")
            return emptyList()
        }

        override suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData> {
            return emptyList()
        }

        override suspend fun saveImages(data: ThumbnailData) {
            Timber.d("mock saveImages() : $data")
        }

        override suspend fun deleteImages(uid: Long) {
            Timber.d("mock saveImages() : $uid")
        }
    }

    companion object {
        private const val QUERY = "bus"
        private const val ERROR_MSG = "Network Request failed!"
    }

    @Test
    fun `should return Success when network request is successful`() = runTest {

        val api = FakeSuccessApi()

        val searchViewModel = SearchViewModel(
            GetRemoteImageUseCase(fakeRepo),
            GetLocalImageUseCase(fakeRepo)
        )

        observeViewModel(searchViewModel)

        assertTrue(receivedUIStates.isEmpty())

        searchViewModel.setUIState(LoadState.Loading)
        try {
            api.getImages(QUERY)
            searchViewModel.setUIState(LoadState.NotLoading(false))
        } catch (exception: Exception) {
            searchViewModel.setUIState(LoadState.Error(exception))
        }

        assertEquals(
            mutableListOf(
                SearchUIEvent.Loading,
                SearchUIEvent.NotLoading
            ),
            receivedUIStates
        )
    }

    @Test
    fun `should return Error when network request fails`() = runTest {
        val api = FakeErrorApi()
        val searchViewModel = SearchViewModel(
            GetRemoteImageUseCase(fakeRepo),
            GetLocalImageUseCase(fakeRepo)
        )

        observeViewModel(searchViewModel)

        assertTrue(receivedUIStates.isEmpty())

        searchViewModel.setUIState(LoadState.Loading)
        try {
            api.getImages(QUERY)
            searchViewModel.setUIState(LoadState.NotLoading(false))
        } catch (exception: Exception) {
            searchViewModel.setUIState(LoadState.Error(exception))
        }

        assertEquals(
            mutableListOf(
                SearchUIEvent.Loading,
                SearchUIEvent.Error(ERROR_MSG)
            ),
            receivedUIStates
        )
    }

    private fun observeViewModel(viewModel: SearchViewModel) {
        viewModel.eventStateForTest.observeForever { uiEvent ->
            if (uiEvent != null) {
                receivedUIStates.add(uiEvent)
            }
        }
    }
}
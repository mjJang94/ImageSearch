package com.mj.imagesearch.usecase.HandleSearchImageSourceUseCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import com.mj.domain.repository.ImageRepository
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.domain.usecase.GetRemoteImageUseCase
import com.mj.imagesearch.ui.main.CommonSearchViewModel
import com.mj.imagesearch.ui.main.CommonSearchViewModel.*
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

    private val receivedUIStates = mutableListOf<UIEvent>()

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

        override suspend fun deleteImages(data: ThumbnailData) {
            Timber.d("mock saveImages() : $data")
        }
    }

    companion object {
        private const val QUERY = "bus"
        private const val ERROR_MSG = "Network Request failed!"
    }

    @Test
    fun `should return Success when network request is successful`() = runTest {

        val api = FakeSuccessApi()

        val viewModel = CommonSearchViewModel(
            GetLocalImageUseCase(fakeRepo),
            GetRemoteImageUseCase(fakeRepo)
        )

        observeViewModel(viewModel)

        assertTrue(receivedUIStates.isEmpty())

        viewModel.setUIState(LoadState.Loading)
        try {
            api.getImages(QUERY)
            viewModel.setUIState(LoadState.NotLoading(false))
        } catch (exception: Exception) {
            viewModel.setUIState(LoadState.Error(exception))
        }

        assertEquals(
            mutableListOf(
                UIEvent.Loading,
                UIEvent.NotLoading
            ),
            receivedUIStates
        )
    }

    @Test
    fun `should return Error when network request fails`() = runTest {
        val api = FakeErrorApi()
        val viewModel = CommonSearchViewModel(
            GetLocalImageUseCase(fakeRepo),
            GetRemoteImageUseCase(fakeRepo)
        )

        observeViewModel(viewModel)

        assertTrue(receivedUIStates.isEmpty())

        viewModel.setUIState(LoadState.Loading)
        try {
            api.getImages(QUERY)
            viewModel.setUIState(LoadState.NotLoading(false))
        } catch (exception: Exception) {
            viewModel.setUIState(LoadState.Error(exception))
        }

        assertEquals(
            mutableListOf(
                UIEvent.Loading,
                UIEvent.Error(ERROR_MSG)
            ),
            receivedUIStates
        )
    }

    private fun observeViewModel(viewModel: CommonSearchViewModel) {
        viewModel.testState.observeForever { uiEvent ->
            if (uiEvent != null) {
                receivedUIStates.add(uiEvent)
            }
        }
    }
}
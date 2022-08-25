package com.mj.imagesearch.usecase.HandleFavoriteImageSourceUseCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mj.data.model.FavoriteImageEntity
import com.mj.domain.ImageRepository
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.HandleFavoriteImageSourceUseCase
import com.mj.imagesearch.resource.getFavoriteMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CommonFavoriteRepositoryTest {

    private val fakeDatabase = FakeDatabase()

    private val fakeRepo = object : ImageRepository {

        override val favoriteImages: LiveData<List<ThumbnailData>>
            get() = fakeDatabase.getAllFavoriteImagesLive().map {
                it.mapToThumbnailData()
            }

        override suspend fun getFavoriteImages(): List<ThumbnailData> {
            return fakeDatabase.getAllFavoriteImages().mapToThumbnailData()
        }

        override suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData> {
            return emptyList()
        }

        override suspend fun saveImages(data: ThumbnailData) {
            fakeDatabase.insert(FavoriteImageEntity(0, data.thumbnail))
        }

        override suspend fun deleteImages(data: ThumbnailData) {
            fakeDatabase.delete(FavoriteImageEntity(0, data.thumbnail))
        }
    }

    @Test
    fun `getAll() should return list of ThumbnailData from database`() = runTest {
        val useCase = HandleFavoriteImageSourceUseCase(fakeRepo)

        assertEquals(getFavoriteMock, useCase.getAll())
    }

    @Test
    fun `insert() should return true after insert data`() = runTest {

        val useCase = HandleFavoriteImageSourceUseCase(fakeRepo)

        val job = async {
            useCase.insert(ThumbnailData(0, "test_thumbnails"))
        }
        job.await()

        assertEquals(true, fakeDatabase.insertedToDb)
    }

    @Test
    fun `delete() should return true after delete from database`() = runTest {

        val useCase = HandleFavoriteImageSourceUseCase(fakeRepo)

        val job = async {
            useCase.delete(ThumbnailData(0, "test_thumbnails"))
        }
        job.await()

        assertEquals(true, fakeDatabase.deleteFromDb)
    }

    private fun List<FavoriteImageEntity>.mapToThumbnailData(): List<ThumbnailData> =
        this.map { data ->
            ThumbnailData(data.uid, data.thumbnail)
        }
}
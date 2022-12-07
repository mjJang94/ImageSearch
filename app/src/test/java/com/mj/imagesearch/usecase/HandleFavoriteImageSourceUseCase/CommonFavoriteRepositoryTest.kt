package com.mj.imagesearch.usecase.HandleFavoriteImageSourceUseCase

import com.mj.data.model.FavoriteImageEntity
import com.mj.domain.model.ThumbnailData
import com.mj.domain.repository.ImageRepository
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.imagesearch.resource.getFavoriteMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CommonFavoriteRepositoryTest {

    private val fakeDatabase = FakeDatabase()

    private val fakeRepo = object : ImageRepository {

        override suspend fun getFavoriteImages(): Flow<List<ThumbnailData>> {
            return flow {
                fakeDatabase.getAllFavoriteImages().collect {
                    emit(it.mapToThumbnailData())
                }
            }
        }

        override suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData> {
            return emptyList()
        }

        override suspend fun saveImages(data: ThumbnailData) {
            fakeDatabase.insert(FavoriteImageEntity(0, data.thumbnail))
        }

        override suspend fun deleteImages(uid: Long) {
            fakeDatabase.delete(uid)
        }
    }

    @Test
    fun `getAll() should return list of ThumbnailData from database`() = runTest {
        val useCase = GetLocalImageUseCase(fakeRepo)
        useCase.getAll().collect {
            assertEquals(getFavoriteMock, it)
        }
    }

    @Test
    fun `insert() should return true after insert data`() = runTest {

        val useCase = GetLocalImageUseCase(fakeRepo)

        val job = async {
            useCase.insert(ThumbnailData(0, "test_thumbnails"))
        }
        job.await()

        assertEquals(true, fakeDatabase.insertedToDb)
    }

    @Test
    fun `delete() should return true after delete from database`() = runTest {

        val useCase = GetLocalImageUseCase(fakeRepo)

        val job = async {
            useCase.delete(0)
        }
        job.await()

        assertEquals(true, fakeDatabase.deleteFromDb)
    }

    private fun List<FavoriteImageEntity>.mapToThumbnailData(): List<ThumbnailData> =
        this.map { data ->
            ThumbnailData(data.uid, data.thumbnail)
        }
}
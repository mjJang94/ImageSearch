package com.mj.imagesearch.usecase.HandleFavoriteImageSourceUseCase

import com.mj.data.local.FavoriteImageDao
import com.mj.data.model.FavoriteImageEntity
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.resource.getFavoriteMock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDatabase : FavoriteImageDao {

    var insertedToDb = false
    var deleteFromDb = false

    override fun getAllFavoriteImages(): Flow<List<FavoriteImageEntity>> {
        return flow{
            emit(getFavoriteMock.mapToFavoriteImageEntity())
        }
    }

    override suspend fun insert(favoriteImageEntity: FavoriteImageEntity) {
        insertedToDb = true
    }

    override suspend fun delete(uid: Long) {
        deleteFromDb = true
    }

    private fun List<ThumbnailData>.mapToFavoriteImageEntity(): List<FavoriteImageEntity> =
        this.map { data ->
            FavoriteImageEntity(data.uid, data.thumbnail)
        }
}
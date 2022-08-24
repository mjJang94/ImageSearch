package com.mj.imagesearch.usecase.HandleFavoriteImageSourceUseCase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mj.data.local.FavoriteImageDao
import com.mj.data.local.FavoriteImageEntity
import com.mj.domain.model.ThumbnailData
import com.mj.imagesearch.resource.getFavoriteMock
import timber.log.Timber

class FakeDatabase : FavoriteImageDao {

    var insertedToDb = false
    var deleteFromDb = false

    override fun getAllFavoriteImagesLive(): LiveData<List<FavoriteImageEntity>> {
        return MutableLiveData(getFavoriteMock.mapToFavoriteImageEntity())
    }

    override fun getAllFavoriteImages(): List<FavoriteImageEntity> {
        return getFavoriteMock.mapToFavoriteImageEntity()
    }

    override suspend fun insert(favoriteImageEntity: FavoriteImageEntity) {
        insertedToDb = true
    }

    override suspend fun delete(favoriteImageEntity: FavoriteImageEntity) {
        deleteFromDb = true
    }

    private fun List<ThumbnailData>.mapToFavoriteImageEntity(): List<FavoriteImageEntity> =
        this.map { data ->
            FavoriteImageEntity(data.uid, data.thumbnail)
        }
}
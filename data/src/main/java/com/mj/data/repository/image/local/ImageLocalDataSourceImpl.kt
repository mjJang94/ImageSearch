package com.mj.data.repository.image.local

import androidx.lifecycle.LiveData
import com.mj.data.local.FavoriteImageDao
import com.mj.data.model.FavoriteImageEntity
import javax.inject.Inject

internal class ImageLocalDataSourceImpl @Inject constructor(private val imageDao: FavoriteImageDao) :
    ImageLocalDataSource {

    override val allFavoriteImages: LiveData<List<FavoriteImageEntity>>
        get() = imageDao.getAllFavoriteImagesLive()

    override suspend fun getAllFavoriteImages(): List<FavoriteImageEntity> =
        imageDao.getAllFavoriteImages()

    override suspend fun saveImages(data: FavoriteImageEntity) {
        imageDao.insert(data)
    }

    override suspend fun deleteImages(uid: Long) {
        imageDao.delete(uid)
    }
}
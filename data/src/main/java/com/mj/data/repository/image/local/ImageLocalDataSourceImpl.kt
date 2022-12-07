package com.mj.data.repository.image.local

import com.mj.data.local.FavoriteImageDao
import com.mj.data.model.FavoriteImageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ImageLocalDataSourceImpl @Inject constructor(private val imageDao: FavoriteImageDao) :
    ImageLocalDataSource {

    override suspend fun getAllFavoriteImages(): Flow<List<FavoriteImageEntity>> =
    flow {
        imageDao.getAllFavoriteImages().collect{
            emit(it)
        }
    }

    override suspend fun saveImages(data: FavoriteImageEntity) {
        imageDao.insert(data)
    }

    override suspend fun deleteImages(uid: Long) {
        imageDao.delete(uid)
    }
}
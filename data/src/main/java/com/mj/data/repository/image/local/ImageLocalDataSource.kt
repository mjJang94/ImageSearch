package com.mj.data.repository.image.local

import com.mj.data.model.FavoriteImageEntity
import kotlinx.coroutines.flow.Flow

interface ImageLocalDataSource {
    suspend fun getAllFavoriteImages(): Flow<List<FavoriteImageEntity>>
    suspend fun saveImages(data: FavoriteImageEntity)
    suspend fun deleteImages(uid: Long)
}
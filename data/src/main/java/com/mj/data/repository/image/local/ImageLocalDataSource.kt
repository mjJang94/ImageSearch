package com.mj.data.repository.image.local

import androidx.lifecycle.LiveData
import com.mj.data.model.FavoriteImageEntity

interface ImageLocalDataSource {
    val allFavoriteImages: LiveData<List<FavoriteImageEntity>>
    suspend fun getAllFavoriteImages(): List<FavoriteImageEntity>
    suspend fun saveImages(data: FavoriteImageEntity)
    suspend fun deleteImages(data: FavoriteImageEntity)
}
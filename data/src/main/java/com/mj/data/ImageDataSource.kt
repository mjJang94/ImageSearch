package com.mj.data

import androidx.lifecycle.LiveData
import com.mj.data.model.FavoriteImageEntity
import com.mj.data.remote.ImageSearchResponse

interface ImageDataSource {
    val allFavoriteImages: LiveData<List<FavoriteImageEntity>>
    suspend fun getAllFavoriteImages(): List<FavoriteImageEntity>
    suspend fun getRemoteImages(query: String, loadSize: Int, start: Int): ImageSearchResponse
    suspend fun saveImages(data: FavoriteImageEntity)
    suspend fun deleteImages(data: FavoriteImageEntity)
}
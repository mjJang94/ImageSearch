package com.mj.domain.repository

import androidx.lifecycle.LiveData
import com.mj.domain.model.ThumbnailData

interface ImageRepository {
    val favoriteImages: LiveData<List<ThumbnailData>>
    suspend fun getFavoriteImages(): List<ThumbnailData>
    suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData>
    suspend fun saveImages(data: ThumbnailData)
    suspend fun deleteImages(uid: Long)
}
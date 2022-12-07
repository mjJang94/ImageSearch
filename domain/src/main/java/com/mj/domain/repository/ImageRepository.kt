package com.mj.domain.repository

import com.mj.domain.model.ThumbnailData
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getFavoriteImages(): Flow<List<ThumbnailData>>
    suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData>
    suspend fun saveImages(data: ThumbnailData)
    suspend fun deleteImages(uid: Long)
}
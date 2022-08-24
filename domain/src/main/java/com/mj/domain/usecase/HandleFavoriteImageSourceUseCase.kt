package com.mj.domain.usecase

import androidx.lifecycle.LiveData
import com.mj.domain.ImageRepository
import com.mj.domain.model.ThumbnailData
import javax.inject.Inject

class HandleFavoriteImageSourceUseCase @Inject constructor(
    private val source: ImageRepository
) {
    val favoriteImages: LiveData<List<ThumbnailData>> = source.favoriteImages

    suspend fun getAll(): List<ThumbnailData> = source.getFavoriteImages()

    suspend fun insert(data: ThumbnailData) {
        source.saveImages(data)
    }

    suspend fun delete(data: ThumbnailData) {
        source.deleteImages(data)
    }
}
package com.mj.domain.usecase

import androidx.lifecycle.LiveData
import com.mj.domain.model.ThumbnailData
import com.mj.domain.repository.ImageRepository
import javax.inject.Inject

class GetLocalImageUseCase @Inject constructor(private val repository: ImageRepository) {

    val favoriteImages: LiveData<List<ThumbnailData>> = repository.favoriteImages

    suspend fun getAll(): List<ThumbnailData> = repository.getFavoriteImages()

    suspend fun insert(data: ThumbnailData) {
        repository.saveImages(data)
    }

    suspend fun delete(data: ThumbnailData) {
        repository.deleteImages(data)
    }
}
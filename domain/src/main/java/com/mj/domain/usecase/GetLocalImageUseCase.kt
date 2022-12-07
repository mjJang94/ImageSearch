package com.mj.domain.usecase

import com.mj.domain.model.ThumbnailData
import com.mj.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalImageUseCase @Inject constructor(private val repository: ImageRepository) {

    suspend fun getAll(): Flow<List<ThumbnailData>> = repository.getFavoriteImages()

    suspend fun insert(data: ThumbnailData) {
        repository.saveImages(data)
    }

    suspend fun delete(uid: Long) {
        repository.deleteImages(uid)
    }
}
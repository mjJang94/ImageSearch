package com.mj.domain.usecase

import com.mj.domain.repository.ImageRepository
import javax.inject.Inject

class GetRemoteImageUseCase @Inject constructor(private val repository: ImageRepository){
    suspend fun search(query: String, size: Int, start: Int) = repository.getRemoteData(query, size, start)
}
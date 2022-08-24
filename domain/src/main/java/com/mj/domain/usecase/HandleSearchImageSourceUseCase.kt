package com.mj.domain.usecase

import com.mj.domain.ImageRepository
import javax.inject.Inject

class HandleSearchImageSourceUseCase @Inject constructor(
    private val source: ImageRepository
){
    suspend fun search(query: String, size: Int, start: Int) = source.getRemoteData(query, size, start)
}
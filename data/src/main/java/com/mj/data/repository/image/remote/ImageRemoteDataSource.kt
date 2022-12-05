package com.mj.data.repository.image.remote

import com.mj.data.model.ImageSearchResponse

interface ImageRemoteDataSource {
    suspend fun getRemoteImages(query: String, loadSize: Int, start: Int): ImageSearchResponse
}
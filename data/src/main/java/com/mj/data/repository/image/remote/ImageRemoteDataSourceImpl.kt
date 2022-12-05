package com.mj.data.repository.image.remote

import com.mj.data.model.ImageSearchResponse
import com.mj.data.remote.NaverImageSearchService
import javax.inject.Inject

internal class ImageRemoteDataSourceImpl @Inject constructor(
    private val service: NaverImageSearchService,
) : ImageRemoteDataSource {

    override suspend fun getRemoteImages(query: String, loadSize: Int, start: Int): ImageSearchResponse =
        service.getImages(query, loadSize, start)
}
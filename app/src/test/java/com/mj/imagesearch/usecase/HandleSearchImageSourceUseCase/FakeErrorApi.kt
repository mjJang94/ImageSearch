package com.mj.imagesearch.usecase.HandleSearchImageSourceUseCase

import com.mj.data.remote.ImageSearchResponse
import com.mj.data.remote.NaverImageSearchService

class FakeErrorApi: NaverImageSearchService {
    override suspend fun getImages(query: String, display: Int?, start: Int?, sort: String?, filter: String?): ImageSearchResponse {
        throw Exception("Network Request failed!")
    }
}
package com.mj.imagesearch.usecase.HandleSearchImageSourceUseCase

import com.mj.data.remote.ImageSearchResponse
import com.mj.data.remote.NaverImageSearchService
import com.mj.imagesearch.resource.getImageMock

class FakeSuccessApi: NaverImageSearchService {

    override suspend fun getImages(query: String, display: Int?, start: Int?, sort: String?, filter: String?): ImageSearchResponse {
      return getImageMock
    }
}
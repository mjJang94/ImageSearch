package com.mj.imagesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetRemoteImageUseCase
import java.lang.Exception

class NaverImageSearchDataSource(
    private val query: String,
    private val handleSearchImageUseCase: GetRemoteImageUseCase
) : PagingSource<Int, ThumbnailData>() {
    override fun getRefreshKey(state: PagingState<Int, ThumbnailData>): Int? {
        return state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ThumbnailData> {
        val start = params.key ?: defaultStart

        return try {
            val response = handleSearchImageUseCase.search(query, 50, start)
            val nextKey = if (response.isEmpty()) null else start + 1
            val prevKey = if (start == defaultStart) null else start - defaultDisplay
            LoadResult.Page(response, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val defaultStart = 1
        const val defaultDisplay = 10
    }
}
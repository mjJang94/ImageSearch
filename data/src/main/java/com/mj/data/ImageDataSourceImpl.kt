package com.mj.data

import com.mj.data.local.FavoriteImageDatabase
import com.mj.data.model.FavoriteImageEntity
import com.mj.data.remote.ImageSearchResponse
import com.mj.data.remote.NaverImageSearchService
import javax.inject.Inject

internal class ImageDataSourceImpl @Inject constructor(
    private val service: NaverImageSearchService,
    private val database: FavoriteImageDatabase
) : ImageDataSource {

    override val allFavoriteImages: LiveData<List<FavoriteImageEntity>>
        get() = database.favoriteImageDao().getAllFavoriteImagesLive()

    override suspend fun getAllFavoriteImages(): List<FavoriteImageEntity> =
        database.favoriteImageDao().getAllFavoriteImages()

    override suspend fun getRemoteImages(query: String, loadSize: Int, start: Int): ImageSearchResponse =
        service.getImages(query, loadSize, start)


    override suspend fun saveImages(data: FavoriteImageEntity) {
        database.favoriteImageDao().insert(data)
    }

    override suspend fun deleteImages(data: FavoriteImageEntity) {
        database.favoriteImageDao().delete(data)
    }
}
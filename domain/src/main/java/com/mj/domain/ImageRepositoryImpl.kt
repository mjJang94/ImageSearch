package com.mj.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mj.data.ImageDataSource
import com.mj.data.model.FavoriteImageEntity
import com.mj.data.model.ItemResponse
import com.mj.domain.model.ThumbnailData
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val datasource: ImageDataSource
) : ImageRepository {

    override suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData> =
        datasource.getRemoteImages(query, loadSize, start).items.itemResponseToThumbnailList()

    override val favoriteImages: LiveData<List<ThumbnailData>>
        get() = datasource.allFavoriteImages.map { data ->
            data.favoriteEntityToThumbnailList()
        }

    override suspend fun getFavoriteImages(): List<ThumbnailData> =
        datasource.getAllFavoriteImages().favoriteEntityToThumbnailList()


    override suspend fun saveImages(data: ThumbnailData) {
        datasource.saveImages(data.mapToFavoriteEntity())
    }

    override suspend fun deleteImages(data: ThumbnailData) {
        datasource.deleteImages(data.mapToFavoriteEntity())
    }

    private fun List<ItemResponse>.itemResponseToThumbnailList(): List<ThumbnailData> =
        this.map {
            ThumbnailData(0, it.thumbnail)
        }

    private fun List<FavoriteImageEntity>.favoriteEntityToThumbnailList(): List<ThumbnailData> =
        this.map {
            ThumbnailData(it.uid, it.thumbnail)
        }

    private fun ThumbnailData.mapToFavoriteEntity(): FavoriteImageEntity =
        FavoriteImageEntity(
            uid = uid,
            thumbnail = thumbnail
        )
}
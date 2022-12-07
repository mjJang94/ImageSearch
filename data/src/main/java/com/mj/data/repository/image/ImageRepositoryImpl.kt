package com.mj.data.repository.image

import android.util.Log
import com.mj.data.model.FavoriteImageEntity
import com.mj.data.model.ItemResponse
import com.mj.data.repository.image.local.ImageLocalDataSource
import com.mj.data.repository.image.remote.ImageRemoteDataSource
import com.mj.domain.model.ThumbnailData
import com.mj.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageRemoteDataSource: ImageRemoteDataSource,
    private val imageLocalDataSource: ImageLocalDataSource
) : ImageRepository {

    override suspend fun getFavoriteImages(): Flow<List<ThumbnailData>> {
        return flow {
            imageLocalDataSource.getAllFavoriteImages().collect{
                emit(it.favoriteEntityToThumbnailList())
            }
        }
    }

    override suspend fun getRemoteData(query: String, loadSize: Int, start: Int): List<ThumbnailData> =
        imageRemoteDataSource.getRemoteImages(query, loadSize, start).items.itemResponseToThumbnailList()

    override suspend fun saveImages(data: ThumbnailData) =
        imageLocalDataSource.saveImages(data.mapToFavoriteEntity())

    override suspend fun deleteImages(uid: Long) =
        imageLocalDataSource.deleteImages(uid)


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
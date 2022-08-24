package com.mj.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteImageEntity: FavoriteImageEntity)

    @Query("select * from favorite_image")
    fun getAllFavoriteImagesLive(): LiveData<List<FavoriteImageEntity>>

    @Query("select * from favorite_image")
    fun getAllFavoriteImages(): List<FavoriteImageEntity>

    @Delete
    suspend fun delete(favoriteImageEntity: FavoriteImageEntity)
}
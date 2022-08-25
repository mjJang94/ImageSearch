package com.mj.data.local

import androidx.room.*
import com.mj.data.model.FavoriteImageEntity

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
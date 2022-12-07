package com.mj.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mj.data.model.FavoriteImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteImageEntity: FavoriteImageEntity)

    @Query("select * from favorite_image")
    fun getAllFavoriteImages(): List<FavoriteImageEntity>

    @Query("delete from favorite_image where uid = :uid")
    suspend fun delete(uid: Long)
}
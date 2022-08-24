package com.mj.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_image")
data class FavoriteImageEntity (
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val thumbnail: String
)

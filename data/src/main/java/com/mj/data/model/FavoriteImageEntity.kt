package com.mj.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_image")
data class FavoriteImageEntity (
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    val thumbnail: String
)

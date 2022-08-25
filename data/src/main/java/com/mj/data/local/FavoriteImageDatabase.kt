package com.mj.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mj.data.model.FavoriteImageEntity

@Database(entities = [FavoriteImageEntity::class], version = 1, exportSchema = false)
abstract class FavoriteImageDatabase : RoomDatabase() {

    abstract fun favoriteImageDao(): FavoriteImageDao

    companion object {

        const val DB_NAME = "favorite_images.db"

        private var INSTANCE: FavoriteImageDatabase? = null

        fun getInstance(context: Context): FavoriteImageDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteImageDatabase::class) {
                    INSTANCE = buildRoomDb(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDb(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FavoriteImageDatabase::class.java,
                DB_NAME
            ).build()

    }
}
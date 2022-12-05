package com.mj.data.di

import android.content.Context
import com.mj.data.local.FavoriteImageDao
import com.mj.data.local.FavoriteImageDatabase
import com.mj.data.repository.image.local.ImageLocalDataSource
import com.mj.data.repository.image.local.ImageLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {

    @Provides
    @Singleton
    fun provideImageLocalDataSource(imageDao: FavoriteImageDao): ImageLocalDataSource =
        ImageLocalDataSourceImpl(imageDao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FavoriteImageDatabase = FavoriteImageDatabase.getInstance(appContext)

    @Provides
    @Singleton
    fun provideImageDao(imageDatabase: FavoriteImageDatabase): FavoriteImageDao = imageDatabase.favoriteImageDao()
}
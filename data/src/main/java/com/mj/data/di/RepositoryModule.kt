package com.mj.data.di

import com.mj.data.repository.image.ImageRepositoryImpl
import com.mj.data.repository.image.local.ImageLocalDataSource
import com.mj.data.repository.image.remote.ImageRemoteDataSource
import com.mj.domain.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideImageRepository(
        imageRemoteDataSource: ImageRemoteDataSource,
        imageLocalDataSource: ImageLocalDataSource
    ): ImageRepository {
        return ImageRepositoryImpl(imageRemoteDataSource, imageLocalDataSource)
    }
}
package com.mj.data.di

import com.mj.data.remote.NaverImageSearchService
import com.mj.data.repository.image.remote.ImageRemoteDataSource
import com.mj.data.repository.image.remote.ImageRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Provides
    @Singleton
    fun imageData(service: NaverImageSearchService): ImageRemoteDataSource =
        ImageRemoteDataSourceImpl(service)
}
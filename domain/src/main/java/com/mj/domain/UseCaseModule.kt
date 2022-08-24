package com.mj.domain

import com.mj.data.ImageDataSource
import com.mj.domain.usecase.HandleFavoriteImageSourceUseCase
import com.mj.domain.usecase.HandleSearchImageSourceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun dataSource(source: ImageDataSource): ImageRepository = ImageRepositoryImpl(source)

    @Provides
    @Singleton
    fun handleFavoriteImageSourceUseCase(repository: ImageRepository) = HandleFavoriteImageSourceUseCase(repository)

    @Provides
    @Singleton
    fun handleSearchImageSourceUseCase(repository: ImageRepository) = HandleSearchImageSourceUseCase(repository)


}
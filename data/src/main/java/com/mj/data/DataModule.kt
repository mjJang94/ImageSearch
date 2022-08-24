package com.mj.data

import android.content.Context
import com.mj.data.local.FavoriteImageDatabase
import com.mj.data.remote.NaverImageSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    const val BASE_URL = "https://openapi.naver.com"
    const val NAVER_CLIENT_ID = "K1qiZunVsQP0Tv4lRBE0"
    const val NAVER_CLIENT_SECRET = "V4Q5mGl270"

    @Provides
    @Singleton
    fun client(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .addHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Provides
    @Singleton
    fun service(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NaverImageSearchService::class.java)

    @Provides
    @Singleton
    fun database(@ApplicationContext appContext: Context) = FavoriteImageDatabase.getInstance(appContext)


    @Provides
    @Singleton
    fun imageData(service: NaverImageSearchService, database: FavoriteImageDatabase): ImageDataSource =
        ImageDataSourceImpl(service, database)
}
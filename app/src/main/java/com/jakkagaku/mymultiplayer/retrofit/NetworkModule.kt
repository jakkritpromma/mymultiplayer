// com/jakkagaku/mymultiplayer/di/NetworkModule.kt
package com.jakkagaku.mymultiplayer.di

import com.jakkagaku.mymultiplayer.repository.LanguageRepository
import com.jakkagaku.mymultiplayer.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = "https://restcountries.com/v3.1/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideLanguageRepository(apiService: ApiService): LanguageRepository =
        LanguageRepository(apiService)
}


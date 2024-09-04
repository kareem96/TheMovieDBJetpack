package com.kareemdev.themoviedb.di

import com.kareemdev.themoviedb.data.remote.retrofit.ApiService
import com.kareemdev.themoviedb.repositories.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(apiService: ApiService): MovieRepository {
        return MovieRepository(apiService)
    }
}
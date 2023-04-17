package com.fsoftstudio.kinopoisklite.data.movies.di

import com.fsoftstudio.kinopoisklite.data.MoviesDataRepository
import com.fsoftstudio.kinopoisklite.data.movies.MoviesDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MoviesRepositoriesModule {

    @Singleton
    @Binds
    fun bindMoviesDataRepository(
        moviesDataRepositoryImpl: MoviesDataRepositoryImpl
    ): MoviesDataRepository
}

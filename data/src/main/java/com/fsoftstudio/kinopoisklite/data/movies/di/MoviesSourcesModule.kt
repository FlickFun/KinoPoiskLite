package com.fsoftstudio.kinopoisklite.data.movies.di

import com.fsoftstudio.kinopoisklite.data.bases.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.movies.sources.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MoviesSourcesModule {

    @Binds
    @Singleton
    fun bindMoviesTMDbRemoteDataSource(
        retrofitMoviesTMDbDataSource: RetrofitMoviesTMDbRemoteDataSource
    ): MoviesTMDbRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
class MoviesDataSourcesModule {

    @Provides
    @Singleton
    fun provideMoviesTMDbApi(retrofit: Retrofit): MoviesTMDbApi =
        retrofit.create(MoviesTMDbApi::class.java)

    @Provides
    @Singleton
    fun provideMoviesTMDbLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): MoviesTMDbLocalDataSource =
        RoomMoviesTMDbLocalDataSource(
            tMDbRoomDatabase.moviesTMDbDao(),
            tMDbRoomDatabase.moviesTMDbFtsDao()
        )
}
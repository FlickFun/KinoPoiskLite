package com.fsoftstudio.kinopoisklite.data.cinema.di

import com.fsoftstudio.kinopoisklite.data.bases.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.cinema.sources.*
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
interface CinemaSourcesModule {

    @Binds
    @Singleton
    fun bindCinemaTMDbRemoteDataSource(
        retrofitCinemaTMDbRemoteDataSource: RetrofitCinemaTMDbRemoteDataSource
    ): CinemaTMDbRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
class CinemaDataSourcesModule {

    @Provides
    @Singleton
    fun provideCinemaTMDbApi(retrofit: Retrofit): CinemaTMDbApi =
        retrofit.create(CinemaTMDbApi::class.java)

    @Provides
    @Singleton
    fun provideCinemaTMDbLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): CinemaTMDbLocalDataSource =
        RoomCinemaTMDbLocalDataSource(
            tMDbRoomDatabase.cinemaTMDbDao()
        )
}
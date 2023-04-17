package com.fsoftstudio.kinopoisklite.data.tvseries.di

import com.fsoftstudio.kinopoisklite.data.bases.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.movies.sources.*
import com.fsoftstudio.kinopoisklite.data.tvseries.sources.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TvSeriesSourcesModule{

    @Binds
    @Singleton
    fun bindTvSeriesTMDbRemoteDataSource(
        retrofitTvSeriesTMDbDataSource: RetrofitTvSeriesTMDbRemoteDataSource
    ): TvSeriesTMDbRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
class TvSeriesDataSourcesModule {

    @Provides
    @Singleton
    fun provideTvSeriesTMDbApi(retrofit: Retrofit): TvSeriesTMDbApi =
        retrofit.create(TvSeriesTMDbApi::class.java)

    @Provides
    @Singleton
    fun provideTvSeriesTMDbLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): TvSeriesTMDbLocalDataSource =
        RoomTvSeriesTMDbLocalDataSource(
            tMDbRoomDatabase.tvSeriesTMDbDao(),
        )
}
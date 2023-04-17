package com.fsoftstudio.kinopoisklite.data.tvseries.di

import com.fsoftstudio.kinopoisklite.data.TvSeriesDataRepository
import com.fsoftstudio.kinopoisklite.data.tvseries.TvSeriesDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TvSeriesRepositoriesModule {

    @Singleton
    @Binds
    fun bindTvSeriesRepository(
        tvSeriesDataRepositoryImpl: TvSeriesDataRepositoryImpl
    ): TvSeriesDataRepository
}

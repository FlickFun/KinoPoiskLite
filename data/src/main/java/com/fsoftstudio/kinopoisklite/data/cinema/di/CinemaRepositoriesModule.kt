package com.fsoftstudio.kinopoisklite.data.cinema.di

import com.fsoftstudio.kinopoisklite.data.CinemaDataRepository
import com.fsoftstudio.kinopoisklite.data.cinema.CinemaDataRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CinemaRepositoriesModule {

    @Singleton
    @Binds
    fun bindCinemaDataRepository(
        cinemaDataRepositoryImp: CinemaDataRepositoryImp
    ): CinemaDataRepository
}

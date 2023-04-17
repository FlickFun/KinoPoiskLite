package com.fsoftstudio.kinopoisklite.data.favorites.di

import com.fsoftstudio.kinopoisklite.data.FavoritesDataRepository
import com.fsoftstudio.kinopoisklite.data.favorites.FavoritesDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoriesModule {

    @Singleton
    @Binds
    fun bindFavoritesDataRepository(
        favoritesDataRepositoryImpl: FavoritesDataRepositoryImpl
    ): FavoritesDataRepository
}

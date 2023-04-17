package com.fsoftstudio.kinopoisklite.data.favorites.di

import com.fsoftstudio.kinopoisklite.data.bases.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.favorites.sources.FavoritesTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.favorites.sources.RoomFavoritesTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.movies.sources.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavoriteSourcesModule {

    @Provides
    @Singleton
    fun provideFavoritesTMDbLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): FavoritesTMDbLocalDataSource =
        RoomFavoritesTMDbLocalDataSource(
            tMDbRoomDatabase.favoriteTMDbDao()
        )
}
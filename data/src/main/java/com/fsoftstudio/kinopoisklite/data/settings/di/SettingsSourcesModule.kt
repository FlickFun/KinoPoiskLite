package com.fsoftstudio.kinopoisklite.data.settings.di

import com.fsoftstudio.kinopoisklite.data.cinema.sources.*
import com.fsoftstudio.kinopoisklite.data.movies.sources.*
import com.fsoftstudio.kinopoisklite.data.settings.sources.FirebaseSettingsRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.settings.sources.SettingsLocalDataSource
import com.fsoftstudio.kinopoisklite.data.settings.sources.SettingsLocalDataSourceImpl
import com.fsoftstudio.kinopoisklite.data.settings.sources.SettingsRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsSourcesModule {

    @Binds
    @Singleton
    fun bindSettingsRemoteDataSource(
        retrofitCinemaTMDbDataSource: FirebaseSettingsRemoteDataSource
    ): SettingsRemoteDataSource

    @Binds
    @Singleton
    fun bindSettingsLocalDataSource(
        settingsLocalDataSourceImpl: SettingsLocalDataSourceImpl
    ): SettingsLocalDataSource

}
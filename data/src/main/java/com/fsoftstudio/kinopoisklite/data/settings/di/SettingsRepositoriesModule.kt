package com.fsoftstudio.kinopoisklite.data.settings.di

import com.fsoftstudio.kinopoisklite.data.SettingsDataRepository
import com.fsoftstudio.kinopoisklite.data.settings.SettingsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsRepositoriesModule {

    @Singleton
    @Binds
    fun bindSettingsDataRepository(
        settingsDataRepositoryImpl: SettingsDataRepositoryImpl
    ): SettingsDataRepository
}

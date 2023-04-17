package com.fsoftstudio.kinopoisklite.data.accounts.di

import com.fsoftstudio.kinopoisklite.data.accounts.sources.AccountsLocalDataSource
import com.fsoftstudio.kinopoisklite.data.accounts.sources.RoomAccountsLocalDataSource
import com.fsoftstudio.kinopoisklite.data.bases.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.movies.sources.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AccountsSourcesModule {

    @Provides
    @Singleton
    fun provideAccountsLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): AccountsLocalDataSource =
        RoomAccountsLocalDataSource(
            tMDbRoomDatabase.accountsDao()
        )
}
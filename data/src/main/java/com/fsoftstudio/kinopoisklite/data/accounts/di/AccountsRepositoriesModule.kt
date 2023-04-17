package com.fsoftstudio.kinopoisklite.data.accounts.di

import com.fsoftstudio.kinopoisklite.data.AccountsDataRepository
import com.fsoftstudio.kinopoisklite.data.accounts.AccountsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountsRepositoriesModule {

    @Singleton
    @Binds
    fun bindAccountsDataRepository(
        accountsDataRepositoryImpl: AccountsDataRepositoryImpl
    ): AccountsDataRepository
}

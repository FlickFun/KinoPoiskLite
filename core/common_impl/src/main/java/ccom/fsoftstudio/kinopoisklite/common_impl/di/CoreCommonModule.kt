package ccom.fsoftstudio.kinopoisklite.common_impl.di

import ccom.fsoftstudio.kinopoisklite.common_impl.AndroidCommonUI
import ccom.fsoftstudio.kinopoisklite.common_impl.AndroidLogger
import com.fsoftstudio.kinopoisklite.common.CommonUi
import com.fsoftstudio.kinopoisklite.common.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreCommonModule {

    @Binds
    @Singleton
    fun bindCommonUi(
        androidCommonUI: AndroidCommonUI
    ): CommonUi

    @Binds
    @Singleton
    fun bindLogger(
        androidLogger: AndroidLogger
    ): Logger
}
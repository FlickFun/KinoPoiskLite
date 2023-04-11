package com.fsoftstudio.kinopoisklite.apps.activities

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.fsoftstudio.kinopoisklite.di.modules.RepositoryModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@MediumTest
class CinemaInfoActivityTest {

    @Test
    fun getShowInfo() {
    }

    @Test
    fun setShowInfo() {
    }

    @Test
    fun getCinemaInfoUseCase() {
    }

    @Test
    fun setCinemaInfoUseCase() {
    }

    @Test
    fun getAppUseCase() {
    }

    @Test
    fun setAppUseCase() {
    }

    @Test
    fun onCreate() {
    }
}
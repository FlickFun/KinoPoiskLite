/**
 * Copyright (C) 2023 Anatoliy Ferin - Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fsoftstudio.kinopoisklite.data.settings

import com.fsoftstudio.kinopoisklite.data.SettingsDataRepository
import com.fsoftstudio.kinopoisklite.data.settings.entity.FirebaseSettingsDataEntity
import com.fsoftstudio.kinopoisklite.data.settings.sources.SettingsRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.settings.sources.SettingsLocalDataSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SettingsDataRepositoryImp @Inject constructor(
    private val settingsRemoteDataSource: SettingsRemoteDataSource,
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsDataRepository {

    override suspend fun getAppData(): Flow<FirebaseSettingsDataEntity?> =
        settingsRemoteDataSource.getAppData()

    override fun getSavedTheme(): Int = settingsLocalDataSource.getSavedTheme()
    override fun saveTheme(theme: Int) = settingsLocalDataSource.saveTheme(theme)
    override fun getTelegramDev(): String? = settingsLocalDataSource.getTelegramDev()
    override fun saveTelegramDev(telegramDev: String) =
        settingsLocalDataSource.saveTelegramDev(telegramDev)

    override fun getBootAutoStart(): Int = settingsLocalDataSource.getBootAutoStart()
    override fun saveBootAutoStart(bootAutoStart: Int) =
        settingsLocalDataSource.saveBootAutoStart(bootAutoStart)

}
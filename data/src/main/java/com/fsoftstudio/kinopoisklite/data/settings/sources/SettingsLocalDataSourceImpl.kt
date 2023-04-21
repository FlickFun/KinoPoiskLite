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
package com.fsoftstudio.kinopoisklite.data.settings.sources

import android.content.SharedPreferences
import android.os.Build
import com.fsoftstudio.kinopoisklite.data.movies.sources.MoviesTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.common.entity.Const.BOOT_AUTO_START_ASC_LATE
import com.fsoftstudio.kinopoisklite.common.entity.Const.KEY_BOOT_AUTO_START
import com.fsoftstudio.kinopoisklite.common.entity.Const.KEY_TELEGRAM_DEV
import com.fsoftstudio.kinopoisklite.common.entity.Const.KEY_THEME
import com.fsoftstudio.kinopoisklite.common.entity.Const.NOTHING
import com.fsoftstudio.kinopoisklite.common.entity.Const.THEME_SYSTEM
import com.fsoftstudio.kinopoisklite.data.Utils.DeleteFiles
import com.fsoftstudio.kinopoisklite.data.cinema.sources.CinemaTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.tvseries.sources.TvSeriesTMDbLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moviesTMDbLocalDataSource: MoviesTMDbLocalDataSource,
    private val tvSeriesTMDbLocalDataSource: TvSeriesTMDbLocalDataSource,
    private val deleteFiles: DeleteFiles,
    private val cinemaTMDbLocalDataSource: CinemaTMDbLocalDataSource
) : SettingsLocalDataSource {

    override fun getSavedTheme(): Int =
        sharedPreferences.getInt(KEY_THEME, THEME_SYSTEM)

    override fun saveTheme(theme: Int) {
        sharedPreferences.edit().putInt(KEY_THEME, theme).apply()
    }

    override fun getTelegramDev(): String? =
        sharedPreferences.getString(KEY_TELEGRAM_DEV, NOTHING)

    override fun saveTelegramDev(telegramDev: String) {
        sharedPreferences.edit().putString(KEY_TELEGRAM_DEV, telegramDev).apply()
    }

    override fun getBootAutoStart(): Int =
        sharedPreferences.getInt(KEY_BOOT_AUTO_START, BOOT_AUTO_START_ASC_LATE)

    override fun saveBootAutoStart(bootAutoStart: Int) {
        sharedPreferences.edit().putInt(KEY_BOOT_AUTO_START, bootAutoStart).apply()
    }

    override fun cleanCache() {
        CoroutineScope(Dispatchers.IO).launch {
            moviesTMDbLocalDataSource.deleteMoviePopularEntities()
            tvSeriesTMDbLocalDataSource.deleteTvSeriesPopularEntities()
            cinemaTMDbLocalDataSource.deleteNotFavoriteCinemaInfoDataEntitiesByIds()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deleteFiles.deleteAllFilesFromPosterDirectory()
            }
        }
    }
}
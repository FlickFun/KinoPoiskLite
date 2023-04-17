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
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.BOOT_AUTO_START_ASC_LATE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.KEY_BOOT_AUTO_START
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.KEY_TELEGRAM_DEV
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.KEY_THEME
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.NOTHING
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.THEME_SYSTEM
import javax.inject.Inject

class SharedPrefsSettingsLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
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
}
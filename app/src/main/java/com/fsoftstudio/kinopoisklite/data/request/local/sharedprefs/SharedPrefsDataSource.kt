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
package com.fsoftstudio.kinopoisklite.data.request.local.sharedprefs

import com.fsoftstudio.kinopoisklite.data.request.local.AppLocalDataSource


class SharedPrefsDataSource(
    private val sharedPrefsRequest: SharedPrefsRequest
) :
    AppLocalDataSource {

    override fun getSavedTheme(): Int = sharedPrefsRequest.getSavedTheme()
    override fun saveTheme(theme: Int) = sharedPrefsRequest.saveTheme(theme)
    override fun getTelegramDev(): String? = sharedPrefsRequest.getTelegramDev()
    override fun saveTelegramDev(telegramDev: String) =
        sharedPrefsRequest.saveTelegramDev(telegramDev)

    override fun getBootAutoStart(): Int = sharedPrefsRequest.getBootAutoStart()
    override fun saveBootAutoStart(bootAutoStart: Int) =
        sharedPrefsRequest.saveBootAutoStart(bootAutoStart)

}
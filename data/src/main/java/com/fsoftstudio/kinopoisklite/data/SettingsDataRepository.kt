package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.settings.entity.FirebaseSettingsDataEntity
import kotlinx.coroutines.flow.Flow

interface SettingsDataRepository {
    suspend fun getAppData() : Flow<FirebaseSettingsDataEntity?>

    fun getSavedTheme() : Int
    fun saveTheme(theme: Int)

    fun getTelegramDev() : String?
    fun saveTelegramDev(telegramDev: String)

    fun getBootAutoStart() : Int
    fun saveBootAutoStart(bootAutoStart: Int)
    fun cleanCache()

}
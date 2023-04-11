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
package com.fsoftstudio.kinopoisklite.domain.usecase

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.domain.data.DataRepository
import com.fsoftstudio.kinopoisklite.domain.ui.UiPosters
import com.fsoftstudio.kinopoisklite.parameters.Sys.BOOT_AUTO_START_ASC_LATE
import com.fsoftstudio.kinopoisklite.parameters.Sys.ERROR_NO_API_KEY_PLEASE_INFORM_DEV
import com.fsoftstudio.kinopoisklite.parameters.Sys.NOTHING
import com.fsoftstudio.kinopoisklite.parameters.Sys.PLEASE_INFORM_DEV
import com.fsoftstudio.kinopoisklite.parameters.Sys.THEME_DARK
import com.fsoftstudio.kinopoisklite.parameters.Sys.THEME_SYSTEM
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.utils.BootAutostartPermissionHelper
import com.fsoftstudio.kinopoisklite.utils.ShowInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import java.io.File
import javax.inject.Inject

class AppUseCase @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val dataRepository: DataRepository,
    private val exceptionsUseCase: ExceptionsUseCase,
    private val showInfo: ShowInfo,
    private val uiPoster: UiPosters
) {

    fun appStarted(ma: MainActivity, compositeDisposable: CompositeDisposable) {
        checkAndCreateDir(ma)
        setApiKey()
        userProfileUseCase.loadUserAndFavorite(compositeDisposable)
        checkForAscBootAutoStart(ma)
    }

    fun setTheme(callback: (Boolean) -> Unit) {
        val savedTheme = dataRepository.getSavedTheme()
        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
        if (savedTheme == THEME_SYSTEM && defaultNightMode != MODE_NIGHT_YES || savedTheme != THEME_DARK ) {
            callback.invoke(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(savedTheme)
            callback.invoke(false)
        }
    }

    fun checkStatusBarColorDark(activity: AppCompatActivity) {
        val window: Window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (activity.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.statusBarColor = activity.getColor(R.color.statusBarDark)
            }
        }
    }

    fun needOpenTelegramDev(parentFragment: Fragment?) {
        dataRepository.getTelegramDev()?.let { telegramDev ->
            if (telegramDev.isNotEmpty() && telegramDev != NOTHING) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramDev))
                    parentFragment?.context?.let { it -> startActivity(it, intent, null) }
                } catch (ex: Exception) {
                    parentFragment?.context?.let { it ->
                        showInfo.toast(it.getString(R.string.link_not_opens), it)
                    }
                }
            }
        }
    }

    private fun checkForAscBootAutoStart(ma: MainActivity) {
        CoroutineScope(Dispatchers.Main).launch {
            if (BootAutostartPermissionHelper.getInstance().isAutoStartPermissionAvailable(ma, true)
                && dataRepository.getBootAutoStart() == BOOT_AUTO_START_ASC_LATE
                && !BootAutostartPermissionHelper.getInstance().isAutoStartEnabled(ma)
            ) {
                delay(60_000L)
                uiPoster.showBootAutoStartAlertDialog(ma)
            }
        }
    }


    private fun checkAndCreateDir(ma: MainActivity) {
        var folder: File
        for (dir in ma.resources.getStringArray(R.array.app_directory)) {
            folder = File(ma.filesDir, dir)
            if (!folder.exists()) {
                folder.mkdir()
            }
        }
    }

    fun setApiKey(isNotNotifyCheckChangePostersWorker: Boolean = true) {
        CoroutineScope(Dispatchers.IO).launch {
            val appData = dataRepository.getAppData().firstOrNull()

            appData?.let {
                if (appData.errorMessage == null) {
                    if (appData.api_key != null) {
                        apiKey = appData.api_key
                        isCanUpdate = true
                    } else {
                        if (isNotNotifyCheckChangePostersWorker) {
                            sendErrorMessageAndRepeatSetApiKey(ERROR_NO_API_KEY_PLEASE_INFORM_DEV)
                        }
                    }
                } else {
                    if (isNotNotifyCheckChangePostersWorker) {
                        sendErrorMessageAndRepeatSetApiKey(appData.errorMessage + PLEASE_INFORM_DEV)
                    }
                }
                if (isNotNotifyCheckChangePostersWorker) {
                    appData.telegram_dev?.let { dataRepository.saveTelegramDev(it) }
                }
            }
        }
    }

    private suspend fun sendErrorMessageAndRepeatSetApiKey(errorMessage: String) {
        exceptionsUseCase.showFireBaseExceptionInfo(errorMessage)
        delay(3_000L)
        setApiKey()
    }

    companion object {

        @JvmStatic
        var isCanUpdate = false

        @JvmStatic
        var apiKey = NOTHING

    }
}
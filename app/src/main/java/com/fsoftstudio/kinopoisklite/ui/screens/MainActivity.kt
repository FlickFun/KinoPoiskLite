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
package com.fsoftstudio.kinopoisklite.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.data.SettingsDataRepository
import com.fsoftstudio.kinopoisklite.databinding.ActivityMainBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.CinemaInfoUseCase
import com.fsoftstudio.kinopoisklite.common.entity.Const.BOOT_AUTO_START_ASC_LATE
import com.fsoftstudio.kinopoisklite.common.entity.Const.BOOT_AUTO_START_DO_NOT_ASC
import com.fsoftstudio.kinopoisklite.common.entity.Const.BOOT_AUTO_START_OPEN_SETTINGS
import com.fsoftstudio.kinopoisklite.utils.BootAutostartPermissionHelper
import com.fsoftstudio.kinopoisklite.utils.ShowAlertDialog
import com.fsoftstudio.kinopoisklite.utils.ShowInfo
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val mainActivityViewModel: MainActivityViewModel by viewModels()

    private val checkNotificationPermission: ActivityResultLauncher<String> by lazy { requestActivityResult() }

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var showInfo: ShowInfo

    @Inject
    lateinit var settingsDataRepository: SettingsDataRepository

    @Inject
    lateinit var cinemaInfoUseCase: CinemaInfoUseCase

    @Inject
    lateinit var appUseCase: AppUseCase

    var isBootAutoStartAlertDialogOpen = true
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mainActivityFirst) {
            mainActivityFirst = false
            appUseCase.setTheme { sameTheme ->
                if (sameTheme) {
                    initMethods()
                }
            }
        } else {
            initMethods()
        }
    }

    private fun initMethods() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appUseCase.checkStatusBarColorDark(this)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        mainActivity = this
        collectViewModel()
        checkPermission()
        appUseCase.appStarted(this, compositeDisposable)

        isFragmentReadyToChangeTheme = true
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }

    fun openCinemaInfo(poster: Poster) {
        cinemaInfoUseCase.openCinemaInfo(poster, this)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun requestActivityResult() = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    private fun collectViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.exception.collect { exception ->
                    exception?.let {
                        if (exception.isNotEmpty()) {
                            showInfo.snackbar(
                                exception,
                                this@MainActivity,
                                binding.clForSnackbarMain
                            )

                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainActivityViewModel.bootAutoStartAlertDialog.collect { bootAutoStartAlertDialog ->
                    bootAutoStartAlertDialog?.let {
                        if(isBootAutoStartAlertDialogOpen){
                            isBootAutoStartAlertDialogOpen = false
                            mainActivityViewModel.setBootAutoStartAlertDialogToStateFlow(null)
                            ShowAlertDialog().showBootAutoStart(this@MainActivity)
                        }
                    }
                }
            }
        }
    }

    fun showAlertDialogAndSaveResult(keyBootAutoStart: Int) {
        when (keyBootAutoStart) {
            BOOT_AUTO_START_DO_NOT_ASC, BOOT_AUTO_START_ASC_LATE -> {
                settingsDataRepository.saveBootAutoStart(keyBootAutoStart)
            }
            BOOT_AUTO_START_OPEN_SETTINGS -> {
                BootAutostartPermissionHelper.getInstance().getAutoStartPermission(
                    this@MainActivity,
                    open = true,
                    newTask = true
                )
                if (BootAutostartPermissionHelper.getInstance().getAutoStartPermission(
                        this@MainActivity,
                        open = false,
                        newTask = false
                    )
                )
                    settingsDataRepository.saveBootAutoStart(
                        if (BootAutostartPermissionHelper.getInstance()
                                .isAutoStartEnabled(this@MainActivity)
                        )
                            BOOT_AUTO_START_DO_NOT_ASC else BOOT_AUTO_START_ASC_LATE
                    )
            }
        }
    }

    companion object {
        @JvmStatic
        var mainActivity: MainActivity? = null

        @JvmStatic
        var mainActivityFirst = true

        @JvmStatic
        var isFragmentReadyToChangeTheme = true
    }
}
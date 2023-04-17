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
package com.fsoftstudio.kinopoisklite.ui.screens.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.data.SettingsDataRepository
import com.fsoftstudio.kinopoisklite.databinding.FragmentProfileBinding
import com.fsoftstudio.kinopoisklite.domain.models.User
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.UserProfileUseCase
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.OK
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.THEME_BATTERY
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.THEME_DARK
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.THEME_LIGHT
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.THEME_SYSTEM
import com.fsoftstudio.kinopoisklite.utils.ShowInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var showInfo: ShowInfo

    @Inject
    lateinit var settingsDataRepository: SettingsDataRepository

    @Inject
    lateinit var appUseCase: AppUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setOnClickListener()
        observeViewModel()
        initTheme()

        if (UserProfileUseCase.user == null) {
            showInsideView(binding.iLayoutAuth.onOffAuthDialog)
        } else {
            setUserDataAndShowProfile()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListener() = with(binding) {
        iLayoutAuth.apply {
            bSignInSignUp.setOnClickListener {
                sendData(it)
            }
            tvSignInSignUpBottom.setOnClickListener {
                changeForm(it)
            }
        }
        iLayoutProfileInfo.bLogout.setOnClickListener {
            exitProfile()
        }
        tvTelegramDeveloper.setOnClickListener {
            appUseCase.needOpenTelegramDev(this@ProfileFragment.parentFragment)
        }
    }

    private fun observeViewModel() {
        profileViewModel.authResult.observe(viewLifecycleOwner) { message ->
            if (message == OK) {
                UserProfileUseCase.user?.let { setUserDataAndShowProfile() }
            } else {
                showInfo.toast(message, this.requireContext())
            }
        }
    }

    private fun setUserDataAndShowProfile() = with(binding.iLayoutProfileInfo) {
        val user = UserProfileUseCase.user
        tvLogin.text = user?.login
        showInsideView(onOffUserInfo)
    }

    private fun showInsideView(showingVies: View) = with(binding) {
        iLayoutProfileInfo.onOffUserInfo.visibility = View.GONE
        iLayoutAuth.onOffAuthDialog.visibility = View.GONE
        showingVies.visibility = View.VISIBLE
    }

    private fun sendData(view: View) = with(binding.iLayoutAuth) {
        val user = User(
            id = 0,
            login = tietLogin.text.toString(),
            password = tietPassword.text.toString(),
            logged = (view as TextView).text == getString(R.string.login)
        )
        profileViewModel.sendToDomainAuthInfo(user)
    }


    private fun changeForm(view: View) = with(binding.iLayoutAuth) {
        val singUp = (view as TextView).text == getString(R.string.signup)

        bSignInSignUp.text = if (singUp) getString(R.string.signup) else getString(R.string.login)
        tvSignInSignUpTitle.text =
            if (singUp) getString(R.string.title_signup) else getString(R.string.title_login)
        tvSignInSignUpBottom.text =
            if (singUp) getString(R.string.login) else getString(R.string.signup)
    }

    private fun exitProfile() {
        profileViewModel.sendToDomainThatExitProfile(UserProfileUseCase.user!!)
        showInsideView(binding.iLayoutAuth.onOffAuthDialog)
    }

    private fun initTheme() = with(binding.iLayoutProfileInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            themeSystem.visibility = View.VISIBLE
            initThemeListener()
        } else {
            themeSystem.visibility = View.GONE
        }
        when (settingsDataRepository.getSavedTheme()) {
            THEME_LIGHT -> themeLight.isChecked = true
            THEME_DARK -> themeDark.isChecked = true
            THEME_SYSTEM -> themeSystem.isChecked = true
            THEME_BATTERY -> themeBattery.isChecked = true
        }
    }

    private fun initThemeListener() = with(binding.iLayoutProfileInfo) {
        themeGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.themeLight -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                R.id.themeDark -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                R.id.themeBattery -> setTheme(
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY, THEME_BATTERY
                )
                R.id.themeSystem -> setTheme(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, THEME_SYSTEM
                )
            }
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        settingsDataRepository.saveTheme(prefsMode)
    }

}
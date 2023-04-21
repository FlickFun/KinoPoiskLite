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

import androidx.lifecycle.ViewModel
import com.fsoftstudio.kinopoisklite.domain.models.ApiError
import com.fsoftstudio.kinopoisklite.domain.ui.UiCinemaInfo
import com.fsoftstudio.kinopoisklite.domain.ui.UiListCinemaSearch
import com.fsoftstudio.kinopoisklite.domain.ui.UiPosters
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_INVALID_API_KEY_PLEASE_INFORM_DEV
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_NO_INTERNET
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_RESOURCE_NOT_FOUND_PLEASE_INFORM_DEV
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_UNKNOWN_PLEASE_INFORM_DEV
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.ui.screens.cinema.CinemaViewModel
import com.google.gson.Gson
import javax.inject.Inject


class ExceptionsUseCase @Inject constructor(
    private val gson: Gson,
    private val uiPoster: UiPosters,
    private val uiListCinemaSearch: UiListCinemaSearch,
    private val uiCinemaInfo: UiCinemaInfo
) {

    fun showExceptionMessage(
        errorMessage: String,
    ) {
        showMessage(EXCEPTION_POSTERS, errorMessage, null)
    }

    fun showHttpExceptionInfo(
        exceptionUseCase: Int,
        errorCode: Int,
        errorBody: String,
        viewModel: ViewModel? = null
    ) {

        val message: String
        val apiError: ApiError = gson.fromJson(errorBody, ApiError::class.java)
        val defaultMessage = "$ERROR_UNKNOWN_PLEASE_INFORM_DEV - $errorCode - $apiError"

        message = when (errorCode) {
            401 -> {
                when (apiError.status_code) {
                    "7" -> {
                        ERROR_INVALID_API_KEY_PLEASE_INFORM_DEV
                    }
                    else -> {
                        apiError.status_message ?: defaultMessage
                    }
                }
            }
            404 -> {
                when (apiError.status_code) {
                    "34" -> {
                        ERROR_RESOURCE_NOT_FOUND_PLEASE_INFORM_DEV
                    }
                    else -> {
                        apiError.status_message ?: defaultMessage
                    }
                }
            }
            else -> apiError.status_message?.let { "$it - $errorCode" } ?: defaultMessage

        }
        showMessage(exceptionUseCase, message, viewModel)
    }

    fun showNoInternet(exceptionUseCase: Int, viewModel: ViewModel? = null) {
        val message = ERROR_NO_INTERNET
        showMessage(exceptionUseCase, message, viewModel)
    }

    fun setEmptyException(exceptionPosters: Int) {
        when (exceptionPosters) {
            EXCEPTION_POSTERS -> {
                MainActivity.mainActivity?.mainActivityViewModel?.setExceptionToStateFlow("")
            }
        }
    }

    private fun showMessage(exceptionUseCase: Int, message: String, viewModel: ViewModel?) {

        val maViewModel = MainActivity.mainActivity?.mainActivityViewModel

        when (exceptionUseCase) {
            EXCEPTION_POSTERS -> {
                maViewModel?.let { uiPoster.showException(message, it) }
            }
            EXCEPTION_LIST_CINEMA_SEARCH -> {
                maViewModel?.let { uiListCinemaSearch.showException(message, it) }
            }
            EXCEPTION_CINEMA_INFO -> {
                (viewModel)?.let { uiCinemaInfo.showException(message, it as CinemaViewModel) }
            }
        }
    }

    companion object {
        const val EXCEPTION_POSTERS = 1
        const val EXCEPTION_LIST_CINEMA_SEARCH = 2
        const val EXCEPTION_CINEMA_INFO = 3

    }
}

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
package com.fsoftstudio.kinopoisklite.ui.screens.cinema

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.domain.usecase.CinemaInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(
    private val cinemaInfoUseCase: CinemaInfoUseCase
) : ViewModel() {

    private var _cinemaInfo = MutableLiveData<CinemaInfo>()
    var cinemaInfo: LiveData<CinemaInfo> = _cinemaInfo

    private var _exception = MutableLiveData<String>()
    var exception: LiveData<String> = _exception

    fun sendCinemaIdToDomain(id: Int, cinema: String) =
        cinemaInfoUseCase.needGetCinemaInfo(id, cinema, this)

    fun setCinemaInfoToLifeData(cinemaInfo: CinemaInfo) = _cinemaInfo.postValue(cinemaInfo)
    fun setExceptionToLifeData(exception: String) = _exception.postValue(exception)
}
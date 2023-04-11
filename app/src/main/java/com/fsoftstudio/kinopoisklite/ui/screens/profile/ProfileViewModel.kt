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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fsoftstudio.kinopoisklite.domain.models.User
import com.fsoftstudio.kinopoisklite.domain.usecase.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val _authResult = MutableLiveData<String>()
    var authResult: LiveData<String> = _authResult

    val compositeDisposable = CompositeDisposable()

    fun sendToDomainAuthInfo(user: User) =
        userProfileUseCase.authUser(user, compositeDisposable, this)

    fun sendToDomainThatExitProfile(user: User) =
        userProfileUseCase.exitProfile(user, compositeDisposable)

    fun setAuthResultToLifeData(message: String) = _authResult.postValue(message)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
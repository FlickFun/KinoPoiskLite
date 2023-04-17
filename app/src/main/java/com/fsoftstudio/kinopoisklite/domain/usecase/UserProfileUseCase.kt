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

import android.annotation.SuppressLint
import com.fsoftstudio.kinopoisklite.data.AccountsDataRepository
import com.fsoftstudio.kinopoisklite.data.FavoritesDataRepository
import com.fsoftstudio.kinopoisklite.domain.mappers.UserMapper
import com.fsoftstudio.kinopoisklite.domain.models.User
import com.fsoftstudio.kinopoisklite.domain.models.mapToRoomUserDataEntity
import com.fsoftstudio.kinopoisklite.domain.ui.UiUserProfile
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.ERROR_EMPTY_FIELD
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.ERROR_LOGIN_ALREADY_EXIST
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.ERROR_WRONG_LOGIN_ORE_PASSWORD
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.LOGIN_GUEST
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.OK
import com.fsoftstudio.kinopoisklite.ui.screens.profile.ProfileViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val favoritesDataRepository: FavoritesDataRepository,
    private val userMapper: UserMapper,
    private val uiUserProfile: UiUserProfile
) {

    private var profileViewModel: ProfileViewModel? = null

    fun loadUserAndFavorite(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(
            accountsDataRepository.getLoggedUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    user = userMapper.fromRoomUserDataEntity(it)
                    loadIdFavoriteCinemaIdsListToHashMap(it.login)
                }, {
                    user = null
                    loadIdFavoriteCinemaIdsListToHashMap(LOGIN_GUEST)
                })
        )
    }

    private fun loadIdFavoriteCinemaIdsListToHashMap(login: String) =
        favoritesDataRepository.loadAllFavoritesEntitiesIdsByLogin(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                ListCinemaFavoriteUseCase.favorite = it.toHashSet()
            }, {

            })

    fun authUser(
        user: User,
        compositeDisposable: CompositeDisposable,
        profileViewModel: ProfileViewModel
    ) {
        this.profileViewModel = profileViewModel

        if (checkFields(user)) {
            compositeDisposable.add(
                accountsDataRepository.getUserInfo(user.login)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (user.logged) {
                            login(user, userMapper.fromRoomUserDataEntity(it), compositeDisposable)
                        } else {
                            sendResultMessage(ERROR_LOGIN_ALREADY_EXIST)
                        }
                    }, {
                        if (user.logged) {
                            sendResultMessage(ERROR_WRONG_LOGIN_ORE_PASSWORD)
                        } else {
                            signup(user, compositeDisposable)
                        }
                    })
            )
        }
    }

    private fun login(user: User, loadedUser: User, compositeDisposable: CompositeDisposable) {
        if (user.login != loadedUser.login || user.password != loadedUser.password) {
            sendResultMessage(ERROR_WRONG_LOGIN_ORE_PASSWORD)
        } else {
            saveLoginUserToDb(loadedUser, compositeDisposable)
        }
    }

    @SuppressLint("CheckResult")
    private fun signup(
        newUser: User,
        compositeDisposable: CompositeDisposable
    ) {
        newUser.id = 0
        saveLoginUserToDb(newUser, compositeDisposable)
    }

    fun exitProfile(user: User, compositeDisposable: CompositeDisposable) {
        saveUserInfoToDb(user, false, compositeDisposable) {
            UserProfileUseCase.user = null
            loadIdFavoriteCinemaIdsListToHashMap(LOGIN_GUEST)
        }
    }

    @SuppressLint("CheckResult")
    private fun saveLoginUserToDb(user: User, compositeDisposable: CompositeDisposable) {
        saveUserInfoToDb(user, true, compositeDisposable) { savedUser ->
            accountsDataRepository.getUserInfo(savedUser.login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    UserProfileUseCase.user = userMapper.fromRoomUserDataEntity(it)
                    loadIdFavoriteCinemaIdsListToHashMap(it.login)
                    sendResultMessage(OK)
                }
        }
    }


    private fun saveUserInfoToDb(
        user: User,
        logged: Boolean,
        compositeDisposable: CompositeDisposable,
        callback: (User) -> Unit
    ) {
        val savedUser = User(
            id = user.id,
            login = user.login,
            password = user.password,
            logged = logged
        )
        compositeDisposable.add(
            accountsDataRepository.saveUserInfo(savedUser.mapToRoomUserDataEntity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.invoke(savedUser)
                }, {

                })

        )
    }

    private fun checkFields(user: User): Boolean {
        if (user.login.isEmpty() || user.password.isEmpty()) {
            sendResultMessage(ERROR_EMPTY_FIELD)
            return false
        }
        return true
    }

    private fun sendResultMessage(message: String) {
        uiUserProfile.showAuthResult(message, profileViewModel!!)
    }

    fun getLogin() = user?.login ?: LOGIN_GUEST

    companion object {
        @JvmStatic
        var user: User? = null
    }
}

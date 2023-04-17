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
package com.fsoftstudio.kinopoisklite.data.accounts.sources

import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomAccountsLocalDataSource @Inject constructor(
    private val accountsDao: AccountsDao
) : AccountsLocalDataSource {

    override fun loadLoggedUserInfoEntity(logged: Boolean): Single<RoomUserDataEntity> =
        accountsDao.loadLoggedUserInfoEntity(logged)

    override fun loadUserInfoEntity(login: String): Single<RoomUserDataEntity> =
        accountsDao.loadUserInfoEntity(login)

    override fun saveUserInfoEntity(roomUserDataEntity: RoomUserDataEntity): Completable =
        accountsDao.addEntityToUserEntityTable(roomUserDataEntity)

}
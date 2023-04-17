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

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity
import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity.Companion.TC_UE_LOGGED
import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity.Companion.TC_UE_LOGIN
import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity.Companion.USER_ENTITY
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface AccountsDao {

    @Query("SELECT * FROM $USER_ENTITY WHERE $TC_UE_LOGGED = :logged")
    fun loadLoggedUserInfoEntity(logged: Boolean): Single<RoomUserDataEntity>

    @Query("SELECT * FROM $USER_ENTITY WHERE $TC_UE_LOGIN = :login")
    fun loadUserInfoEntity(login: String): Single<RoomUserDataEntity>

    @Insert(entity = RoomUserDataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addEntityToUserEntityTable(roomUserDataEntity: RoomUserDataEntity): Completable


}
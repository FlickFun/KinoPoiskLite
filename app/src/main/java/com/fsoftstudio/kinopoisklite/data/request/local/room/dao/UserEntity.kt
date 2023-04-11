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
package com.fsoftstudio.kinopoisklite.data.request.local.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity.Companion.USER_ENTITY
import com.fsoftstudio.kinopoisklite.domain.models.User

@Entity(tableName = USER_ENTITY)
class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "logged")
    val logged: Boolean
) {
    companion object {
        const val USER_ENTITY = "user_list_entities_table"
        const val TC_UE_LOGGED = "logged"
        const val TC_UE_LOGIN = "login"
    }
}

fun UserEntity.mapToUser(): User {
    return User(
        id = this.id,
        login = this.login,
        password = this.password,
        logged = this.logged
    )
}

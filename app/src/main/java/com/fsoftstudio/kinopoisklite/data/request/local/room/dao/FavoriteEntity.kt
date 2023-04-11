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
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity.Companion.FAVORITE_ENTITY

@Entity(tableName = FAVORITE_ENTITY)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "unique_id")
    val uniqueId: Int,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "login")
    val login: String
)  {
    companion object {
        const val FAVORITE_ENTITY = "favorite_list_entities_table"
        const val TC_FE_ID = "id"
        const val TC_FE_LOGIN = "login"
    }
}

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
package com.fsoftstudio.kinopoisklite.data.cinema.entities

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntityFTS4.Companion.CINEMA_INFO_ENTITY_FTS4

@Entity(tableName = CINEMA_INFO_ENTITY_FTS4)
@Fts4(contentEntity = RoomCinemaInfoDataEntity::class, tokenizer = FtsOptions.TOKENIZER_ICU)
class RoomCinemaInfoDataEntityFTS4(
    @PrimaryKey @ColumnInfo(name = "rowid")
    val rowId: Int,
    @ColumnInfo(name = "title")
    val title: String
) {
    companion object {
        const val CINEMA_INFO_ENTITY_FTS4 = "cinema_info_list_entities_table_fts4"
        const val FULL_CIE_FTS4_ROWID = "${CINEMA_INFO_ENTITY_FTS4}.rowid"
        const val FULL_CIE_FTS4_TITLE = "${CINEMA_INFO_ENTITY_FTS4}.title"
    }
}
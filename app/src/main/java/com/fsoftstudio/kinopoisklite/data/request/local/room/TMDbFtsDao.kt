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
package com.fsoftstudio.kinopoisklite.data.request.local.room

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.*
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.FULL_CIE_CINEMA
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.FULL_CIE_ROWID
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.TC_CIE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntityFTS4.Companion.CINEMA_INFO_ENTITY_FTS4
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntityFTS4.Companion.FULL_CIE_FTS4_ROWID
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntityFTS4.Companion.FULL_CIE_FTS4_TITLE

@Dao
interface TMDbFtsDao {
    @Query("SELECT * FROM $CINEMA_INFO_ENTITY JOIN $CINEMA_INFO_ENTITY_FTS4 ON $FULL_CIE_ROWID == $FULL_CIE_FTS4_ROWID WHERE $FULL_CIE_FTS4_TITLE MATCH :searchText AND $FULL_CIE_CINEMA = :cinema ORDER BY $TC_CIE_POPULARITY DESC")
    suspend fun loadMovieEntityByTextFTS4(
        cinema: String,
        searchText: String
    ): List<CinemaInfoEntity>
}
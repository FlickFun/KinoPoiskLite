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
package com.fsoftstudio.kinopoisklite.data.cinema.sources

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_ID

@Dao
interface CinemaTMDbDao {

    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_ID = :id")
    suspend fun loadCinemaInfoEntity(id: Int): RoomCinemaInfoDataEntity

    @Insert(entity = RoomCinemaInfoDataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCinemaInfoEntity(roomCinemaInfoDataEntity: RoomCinemaInfoDataEntity)

    @Query("SELECT * FROM $CINEMA_INFO_ENTITY")
    suspend fun loadAllRoomCinemaInfoEntities(): List<RoomCinemaInfoDataEntity>

    @Delete (entity = RoomCinemaInfoDataEntity::class)
    suspend fun deleteListRoomCinemaInfoDataEntities(list: List<RoomCinemaInfoDataEntity>)
}
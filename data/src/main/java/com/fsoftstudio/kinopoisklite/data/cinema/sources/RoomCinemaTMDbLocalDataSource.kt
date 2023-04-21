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

import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity

class RoomCinemaTMDbLocalDataSource(
    private val cinemaTMDbDao: CinemaTMDbDao
) : CinemaTMDbLocalDataSource {

    override suspend fun loadCinemaInfoEntity(id: Int): RoomCinemaInfoDataEntity =
        cinemaTMDbDao.loadCinemaInfoEntity(id)

    override suspend fun saveCinemaInfoEntity(cinemaInfoEntity: RoomCinemaInfoDataEntity) {
        cinemaTMDbDao.saveCinemaInfoEntity(cinemaInfoEntity)
    }

    override suspend fun deleteNotFavoriteCinemaInfoDataEntitiesByIds() {
        cinemaTMDbDao.deleteListRoomCinemaInfoDataEntities(
            cinemaTMDbDao.loadAllRoomCinemaInfoEntities()
                .filter { e -> !FavoriteIdsStorage.get().contains(e.id) })
    }
}
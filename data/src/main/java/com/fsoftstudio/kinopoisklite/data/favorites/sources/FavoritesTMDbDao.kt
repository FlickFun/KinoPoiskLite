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
package com.fsoftstudio.kinopoisklite.data.favorites.sources

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_CINEMA
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_ID
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity.Companion.FAVORITES_ENTITY
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity.Companion.TC_FE_ID
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity.Companion.TC_FE_LOGIN
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoritesTMDbDao {


    @Query("SELECT $TC_FE_ID FROM $FAVORITES_ENTITY WHERE $TC_FE_LOGIN = :login")
    fun loadAllFavoritesEntitiesIdsByLogin(login: String): Single<List<Int>>

    @Insert(entity = RoomFavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addEntityToFavoritesEntitiesTable(favoriteEntity: RoomFavoritesEntity): Completable

    @Query("DELETE FROM $FAVORITES_ENTITY WHERE $TC_FE_ID = :id AND $TC_FE_LOGIN = :login")
    fun deleteEntityFromFavoritesEntitiesTable(id: Int, login: String): Completable

    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_ID IN (:ids) ORDER BY $TC_CIE_POPULARITY DESC")
    fun loadFavoriteCinemaEntitiesByIdArray(
        cinema: String,
        ids: IntArray
    ): Single<List<RoomCinemaInfoDataEntity>>

}
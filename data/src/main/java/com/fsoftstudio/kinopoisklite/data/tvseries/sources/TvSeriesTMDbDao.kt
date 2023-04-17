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
package com.fsoftstudio.kinopoisklite.data.tvseries.sources

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_CINEMA
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_TITLE
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RoomTvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RoomTvSeriesPosterEntity.Companion.TC_TSPE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RoomTvSeriesPosterEntity.Companion.TV_SERIES_POSTER_ENTITY
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TvSeriesTMDbDao {

    @Query("SELECT * FROM $TV_SERIES_POSTER_ENTITY ORDER BY $TC_TSPE_POPULARITY DESC")
    fun loadTvSeriesPosterEntities(): Single<List<RoomTvSeriesPosterEntity>>

    @Insert(entity = RoomTvSeriesPosterEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveTvSeriesPosterEntities(tvSeriesPopularListEntities: List<RoomTvSeriesPosterEntity>): Completable


    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_TITLE LIKE '%' || :searchText || '%' ORDER BY $TC_CIE_POPULARITY DESC")
    fun loadTvSeriesEntityByText(cinema: String, searchText: String): Single<List<RoomCinemaInfoDataEntity>>

}
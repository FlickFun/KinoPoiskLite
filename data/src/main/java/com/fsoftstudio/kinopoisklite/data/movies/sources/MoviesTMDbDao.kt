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
package com.fsoftstudio.kinopoisklite.data.movies.sources

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_CINEMA
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.TC_CIE_TITLE
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity.Companion.MOVIE_POSTER_ENTITY
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity.Companion.TC_MPE_POPULARITY

@Dao
interface MoviesTMDbDao {

    @Query("SELECT * FROM $MOVIE_POSTER_ENTITY ORDER BY $TC_MPE_POPULARITY DESC")
    suspend fun loadMoviePosterEntities(): List<RoomMoviePosterDataEntity>

    @Insert(entity = RoomMoviePosterDataEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoviePosterEntities(moviePopularListEntities: List<RoomMoviePosterDataEntity>)

    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_TITLE LIKE '%' || :searchText || '%' ORDER BY $TC_CIE_POPULARITY DESC")
    suspend fun loadMovieEntityByText(cinema: String, searchText: String): List<RoomCinemaInfoDataEntity>

}
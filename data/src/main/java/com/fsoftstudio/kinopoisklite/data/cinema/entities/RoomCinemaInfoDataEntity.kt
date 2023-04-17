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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesPosterDataEntity

@Entity(tableName = CINEMA_INFO_ENTITY)
class RoomCinemaInfoDataEntity(
    @PrimaryKey @ColumnInfo(name = "rowid")
    val rowId: Int,
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "popularity")
    val popularityWeight: Float,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "runtime")
    val runtime: String?,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "genres_str")
    val genresStr: String?,
    @ColumnInfo(name = "actors_str")
    val actorsStr: String?,
    @ColumnInfo(name = "cinema")
    val cinema: String
) {
    companion object {
        const val CINEMA_INFO_ENTITY = "cinema_info_list_entities_table"
        const val FULL_CIE_ROWID = "$CINEMA_INFO_ENTITY.rowid"
        const val FULL_CIE_CINEMA = "$CINEMA_INFO_ENTITY.cinema"
        const val TC_CIE_ID = "id"
        const val TC_CIE_CINEMA = "cinema"
        const val TC_CIE_TITLE = "title"
        const val TC_CIE_POPULARITY = "popularity"
    }
}

fun RoomCinemaInfoDataEntity.mapToRetrofitMovieDataEntity(): RetrofitMoviePosterDataEntity {
    return RetrofitMoviePosterDataEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        popularityWeight = this.popularityWeight
    )
}

fun RoomCinemaInfoDataEntity.mapToRetrofitTvSeriesPosterDataEntity(): RetrofitTvSeriesPosterDataEntity {
    return RetrofitTvSeriesPosterDataEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        popularityWeight = this.popularityWeight
    )
}
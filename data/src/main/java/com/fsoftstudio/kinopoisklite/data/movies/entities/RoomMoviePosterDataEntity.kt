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
package com.fsoftstudio.kinopoisklite.data.movies.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity.Companion.MOVIE_POSTER_ENTITY

@Entity(tableName = MOVIE_POSTER_ENTITY)
class RoomMoviePosterDataEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "popularity")
    val popularityWeight: Float
    ) {
    companion object{
        const val MOVIE_POSTER_ENTITY = "movies_poster_list_entities_table"
        const val TC_MPE_POPULARITY = "popularity"
    }
}

fun RoomMoviePosterDataEntity.mapToRemote(): RetrofitMoviePosterDataEntity {
    return RetrofitMoviePosterDataEntity(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        popularityWeight = this.popularityWeight
    )
}
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
package com.fsoftstudio.kinopoisklite.data.request.remote.responses

import com.fsoftstudio.kinopoisklite.KeepFieldNames
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.parameters.Sys.MOVIE
import com.fsoftstudio.kinopoisklite.parameters.Sys.NO_DATA
import com.fsoftstudio.kinopoisklite.parameters.Sys.TV_SERIES
import com.google.gson.annotations.SerializedName

data class ResponseCinemaInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val titleMovie: String?,
    @SerializedName("name")
    val titleTvSeries: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("popularity")
    val popularityWeight: Float?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("runtime")
    val runtime: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("genres")
    val genres: List<Genres>? = null,
    @SerializedName("genresStr")
    val genresStr: String? = null,
    @SerializedName("actorsStr")
    val actorsStr: String? = null
) : KeepFieldNames

data class Genres(
    @SerializedName("name")
    val name: String
) : KeepFieldNames

fun ResponseCinemaInfo.mapToEntity(cinemaInfo: CinemaInfo, cinema: String): CinemaInfoEntity {

    return CinemaInfoEntity(
        rowId = id + if (cinema == TV_SERIES) 1_000_000_000 else 0,
        id = this.id,
        title = if (cinema == MOVIE) this.titleMovie!! else this.titleTvSeries!!,
        posterPath = this.posterPath,
        popularityWeight = this.popularityWeight,
        releaseDate = this.releaseDate,
        runtime = this.runtime,
        overview = this.overview,
        genresStr = cinemaInfo.genres ?: NO_DATA,
        actorsStr = cinemaInfo.actors ?: NO_DATA,
        cinema = cinema

    )
}

fun ResponseCinemaInfo.mapToCinemaInfo(responseCinemaActorsList: ResponseCinemaActorsList?): CinemaInfo {
    return CinemaInfo(
        title = this.titleMovie ?: this.titleTvSeries,
        releaseDate = this.releaseDate ?: NO_DATA,
        runtime = this.runtime ?: NO_DATA,
        genres = getGenres(this),
        actors = responseCinemaActorsList?.let { getActors(it) } ?: NO_DATA,
        oveview = this.overview ?: NO_DATA
    )
}

fun getActors(responseCinemaActorsList: ResponseCinemaActorsList): String {

    if (responseCinemaActorsList.cast.isEmpty()) {
        return NO_DATA
    }
    var strActors = ""
    val countActors = responseCinemaActorsList.cast.size

    val first = if (countActors > 5) {
        responseCinemaActorsList.cast.take(5).toMutableList() + (responseCinemaActorsList.cast
            .drop(5).filter { e -> e.popularityWeight > 3 })
    } else {
        responseCinemaActorsList.cast
    }

    first.forEach { e -> strActors += "${e.name}, " }

    return if (strActors != "") strActors.dropLast(2) else NO_DATA
}


fun getGenres(responseCinemaInfo: ResponseCinemaInfo): String {
    var strGenres = ""

    responseCinemaInfo.genres?.forEach { e -> strGenres += "${e.name}, " }
    return strGenres.dropLast(2)
}


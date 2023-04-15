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
import com.fsoftstudio.kinopoisklite.parameters.Sys.NO_TITLE
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
        title = setNoTitleIfNullOrEmpty(if (cinema == MOVIE) this.titleMovie else this.titleTvSeries),
        posterPath = setNullIfEmpty(this.posterPath),
        popularityWeight = this.popularityWeight ?: 0F,
        releaseDate = setNullIfEmpty(this.releaseDate),
        runtime = setNullIfEmpty(this.runtime),
        overview = setNullIfEmpty(this.overview),

        genresStr = setNullIfNoData(cinemaInfo.genres),
        actorsStr = setNullIfNoData(cinemaInfo.actors),

        cinema = cinema
    )
}

fun ResponseCinemaInfo.mapToCinemaInfo(responseCinemaActorsList: ResponseCinemaActorsList?): CinemaInfo {
    return CinemaInfo(
        title = setNoTitleIfNullOrEmpty(this.titleMovie ?: this.titleTvSeries),

        releaseDate = setNoDataIfNullOrEmpty(this.releaseDate),
        runtime = setNoDataIfNullOrEmpty(this.runtime),

        genres = setNoDataIfNullOrEmpty(getGenres(this)),
        actors = setNoDataIfNullOrEmpty(responseCinemaActorsList?.let { getActors(it) }),

        oveview = setNoDataIfNullOrEmpty(this.overview)
    )
}

private fun setNullIfNoData(data: String) =
    if (data == NO_DATA) null else data
private fun setNullIfEmpty(data: String?) =
    if (data.isNullOrEmpty()) null else data
private fun setNoDataIfNullOrEmpty(data: String?) =
    if (data.isNullOrEmpty()) NO_DATA else data
private fun setNoTitleIfNullOrEmpty(data: String?) =
    if (data.isNullOrEmpty()) NO_TITLE else data

fun getActors(responseCinemaActorsList: ResponseCinemaActorsList): String? {

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
    return if (strActors.isNotEmpty()) strActors.dropLast(2) else null
}


fun getGenres(responseCinemaInfo: ResponseCinemaInfo): String? {
    var strGenres = ""
    responseCinemaInfo.genres?.forEach { e -> strGenres += "${e.name}, " }
    return if (strGenres.isNotEmpty()) strGenres.dropLast(2) else null
}


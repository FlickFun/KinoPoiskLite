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

import com.fsoftstudio.kinopoisklite.data.parameters.KeepFieldNames
import com.google.gson.annotations.SerializedName

data class RetrofitCinemaInfoDataEntity(
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
    val genres: List<RetrofitGenresDataEntity>? = null,
    @SerializedName("genresStr")
    val genresStr: String? = null,
    @SerializedName("actorsStr")
    val actorsStr: String? = null
) : KeepFieldNames

data class RetrofitGenresDataEntity(
    @SerializedName("name")
    val name: String
) : KeepFieldNames

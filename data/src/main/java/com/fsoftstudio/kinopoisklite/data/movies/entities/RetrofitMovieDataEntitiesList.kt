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

import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.NO_DATA
import com.google.gson.annotations.SerializedName

data class RetrofitMovieDataEntitiesList(
    @SerializedName("results")
    val results: List<RetrofitMoviePosterDataEntity>
)

data class RetrofitMoviePosterDataEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("popularity")
    val popularityWeight: Float?
)

fun RetrofitMoviePosterDataEntity.mapToLocal(): RoomMoviePosterDataEntity {
    return RoomMoviePosterDataEntity(
        id = this.id,
        title = this.title ?: NO_DATA,
        posterPath = this.posterPath,
        popularityWeight = this.popularityWeight ?: 0.0F
    )
}

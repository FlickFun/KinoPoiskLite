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

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity
import kotlinx.coroutines.flow.Flow

interface MoviesTMDbLocalDataSource {

    suspend fun loadMoviePopularEntities(): List<RoomMoviePosterDataEntity>

    suspend fun saveListMoviesPosters(listRetrofitMoviePosterDataEntity: List<RetrofitMoviePosterDataEntity>?)

    suspend fun searchMovieEntitiesByText(cinema: String, searchText: String): Flow<List<RoomCinemaInfoDataEntity>>

    suspend fun deleteMoviePopularEntities()
}

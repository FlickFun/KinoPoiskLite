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
import com.fsoftstudio.kinopoisklite.data.movies.entities.mapToLocal
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomMoviesTMDbLocalDataSource(
    private val moviesTMDbDao: MoviesTMDbDao,
    private val moviesTMDbFtsDao: MoviesTMDbFtsDao,
) : MoviesTMDbLocalDataSource {

    override suspend fun loadMoviePopularEntities(): List<RoomMoviePosterDataEntity> =
        moviesTMDbDao.loadMoviePosterEntities()

    override suspend fun saveListMoviesPosters(listRetrofitMoviePosterDataEntity: List<RetrofitMoviePosterDataEntity>?) {
        listRetrofitMoviePosterDataEntity?.let {
            moviesTMDbDao.saveMoviePosterEntities(
                it.map { moviePoster -> moviePoster.mapToLocal() }
            )
        }
    }

    override suspend fun searchMovieEntitiesByText(
        cinema: String,
        searchText: String
    ): Flow<List<RoomCinemaInfoDataEntity>> = flow {
        if (searchText.isNotEmpty()) {
            val entities = moviesTMDbFtsDao.loadMovieEntityByTextFTS4(cinema, "$searchText*")
            if (entities.isNotEmpty()) {
                emit(entities)
            }
        }
        emit(moviesTMDbDao.loadMovieEntityByText(cinema, searchText))
    }
}
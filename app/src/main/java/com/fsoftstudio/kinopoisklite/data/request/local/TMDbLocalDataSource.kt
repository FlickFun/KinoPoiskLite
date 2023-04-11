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
package com.fsoftstudio.kinopoisklite.data.request.local

import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.MoviePosterEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.MoviePoster
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.TvSeriesPoster
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface TMDbLocalDataSource {

    suspend fun loadMoviePopularEntities(): List<MoviePosterEntity>
    suspend fun saveListMoviesPosters(listMoviePoster: List<MoviePoster>?)

    fun loadTvSeriesPopularEntities(): Single<List<TvSeriesPosterEntity>>
    fun saveListTvSeriesPosters(listTvSeriesPoster: List<TvSeriesPoster>): Completable

    suspend fun searchMovieEntitiesByText(cinema: String, searchText: String): Flow<List<CinemaInfoEntity>>
    fun searchTvSeriesEntitiesByText(cinema: String, searchText: String): Single<List<CinemaInfoEntity>>

    suspend fun loadCinemaInfoEntity(id: Int): CinemaInfoEntity
    suspend fun saveCinemaInfoEntity(cinemaInfoEntity: CinemaInfoEntity)

    fun loadAllFavoriteEntitiesIdsByLogin(login: String): Single<List<Int>>
    fun addEntityToFavoriteEntities(id: Int, login: String): Completable
    fun deleteEntityFromFavoriteEntities(id: Int, login: String): Completable

    fun loadFavoriteMovieEntities(cinema: String, ids: List<Int>): Single<List<CinemaInfoEntity>>
    fun loadFavoriteTvSeriesEntities(cinema: String, ids: List<Int>): Single<List<CinemaInfoEntity>>

    fun loadLoggedUserInfoEntity(logged: Boolean): Single<UserEntity>
    fun loadUserInfoEntity(login: String): Single<UserEntity>
    fun saveUserInfoEntity(userEntity: UserEntity): Completable

}

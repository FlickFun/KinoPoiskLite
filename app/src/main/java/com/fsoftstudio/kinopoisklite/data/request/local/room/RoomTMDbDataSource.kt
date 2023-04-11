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
package com.fsoftstudio.kinopoisklite.data.request.local.room

import com.fsoftstudio.kinopoisklite.data.request.local.TMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.*
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.mapToLocal
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.MoviePoster
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.TvSeriesPoster
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomTMDbDataSource(
    private val tMDbDao: TMDbDao,
    private val tMDbFtsDao: TMDbFtsDao,
) : TMDbLocalDataSource {

    override suspend fun loadMoviePopularEntities(): List<MoviePosterEntity> =
        tMDbDao.loadMoviePosterEntities()

    override suspend fun saveListMoviesPosters(listMoviePoster: List<MoviePoster>?) {
        listMoviePoster?.let {
            tMDbDao.saveMoviePosterEntities(
                it.map { moviePoster -> moviePoster.mapToLocal() }
            )
        }
    }

    override fun loadTvSeriesPopularEntities(): Single<List<TvSeriesPosterEntity>> =
        tMDbDao.loadTvSeriesPosterEntities()

    override fun saveListTvSeriesPosters(listTvSeriesPoster: List<TvSeriesPoster>): Completable =
        tMDbDao.saveTvSeriesPosterEntities(
            listTvSeriesPoster.map { tvSeriesPoster -> tvSeriesPoster.mapToLocal() }
        )

    override suspend fun searchMovieEntitiesByText(
        cinema: String,
        searchText: String
    ): Flow<List<CinemaInfoEntity>> = flow {
        if (searchText.isNotEmpty()) {
            val entities = tMDbFtsDao.loadMovieEntityByTextFTS4(cinema, "$searchText*")
            if (entities.isNotEmpty()) {
                emit(entities)
            }
        }
        emit(tMDbDao.loadMovieEntityByText(cinema, searchText))
    }

    override fun searchTvSeriesEntitiesByText(
        cinema: String,
        searchText: String
    ): Single<List<CinemaInfoEntity>> = tMDbDao.loadTvSeriesEntityByText(cinema, searchText)


    override suspend fun loadCinemaInfoEntity(id: Int): CinemaInfoEntity =
        tMDbDao.loadCinemaInfoEntity(id)

    override suspend fun saveCinemaInfoEntity(cinemaInfoEntity: CinemaInfoEntity) {
        tMDbDao.saveCinemaInfoEntity(cinemaInfoEntity)
    }

    override fun loadAllFavoriteEntitiesIdsByLogin(login: String): Single<List<Int>> =
        tMDbDao.loadAllFavoriteEntitiesIdsByLogin(login)

    override fun addEntityToFavoriteEntities(id: Int, login: String): Completable =
        tMDbDao.addEntityToFavoriteEntitiesTable(
            FavoriteEntity(
                uniqueId = 0,
                id = id,
                login = login
            )
        )

    override fun deleteEntityFromFavoriteEntities(id: Int, login: String): Completable =
        tMDbDao.deleteEntityFromFavoriteEntitiesTable(id, login)


    override fun loadFavoriteMovieEntities(
        cinema: String,
        ids: List<Int>
    ): Single<List<CinemaInfoEntity>> =
        tMDbDao.loadFavoriteCinemaEntitiesByIdArray(cinema, ids.toIntArray())

    override fun loadFavoriteTvSeriesEntities(
        cinema: String,
        ids: List<Int>
    ): Single<List<CinemaInfoEntity>> =
        tMDbDao.loadFavoriteCinemaEntitiesByIdArray(cinema, ids.toIntArray())


    override fun loadLoggedUserInfoEntity(logged: Boolean): Single<UserEntity> =
        tMDbDao.loadLoggedUserInfoEntity(logged)

    override fun loadUserInfoEntity(login: String): Single<UserEntity> =
        tMDbDao.loadUserInfoEntity(login)

    override fun saveUserInfoEntity(userEntity: UserEntity): Completable =
        tMDbDao.addEntityToUserEntityTable(userEntity)

}
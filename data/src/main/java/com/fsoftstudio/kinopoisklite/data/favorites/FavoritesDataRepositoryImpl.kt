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
package com.fsoftstudio.kinopoisklite.data.favorites

import com.fsoftstudio.kinopoisklite.data.FavoritesDataRepository
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.favorites.sources.FavoritesTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.common.entity.Const.MOVIE
import com.fsoftstudio.kinopoisklite.common.entity.Const.TV_SERIES
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavoritesDataRepositoryImpl @Inject constructor(
    private val favoritesTMDbLocalDataSource: FavoritesTMDbLocalDataSource,
) : FavoritesDataRepository {


    override fun loadAllFavoritesEntitiesIdsByLogin(login: String): Single<List<Int>> =
        favoritesTMDbLocalDataSource.loadAllFavoritesEntitiesIdsByLogin(login)

    override fun addEntityIdToFavoritesEntities(id: Int, login: String): Completable =
        favoritesTMDbLocalDataSource.addEntityToFavoritesEntities(id, login)

    override fun deleteEntityIdFromFavoritesEntities(id: Int, login: String): Completable =
        favoritesTMDbLocalDataSource.deleteEntityFromFavoritesEntities(id, login)

    override fun getFavoritesMoviePostersListByLogin(login: String): Observable<List<RoomCinemaInfoDataEntity>> =
        loadAllFavoritesEntitiesIdsByLogin(login).toObservable()
            .flatMap { intList ->
                favoritesTMDbLocalDataSource.loadFavoritesMovieEntities(MOVIE, intList).toObservable()
            }

    override fun getFavoritesTvSeriesPostersListByLogin(login: String): Observable<List<RoomCinemaInfoDataEntity>> =
        loadAllFavoritesEntitiesIdsByLogin(login).toObservable()
            .flatMap { intList ->
                favoritesTMDbLocalDataSource.loadFavoritesTvSeriesEntities(TV_SERIES, intList).toObservable()
            }
}
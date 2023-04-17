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
package com.fsoftstudio.kinopoisklite.data.favorites.sources

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomFavoritesTMDbLocalDataSource @Inject constructor(
    private val favoritesTMDbDao: FavoritesTMDbDao
) : FavoritesTMDbLocalDataSource {

    override fun loadAllFavoritesEntitiesIdsByLogin(login: String): Single<List<Int>> =
        favoritesTMDbDao.loadAllFavoritesEntitiesIdsByLogin(login)

    override fun addEntityToFavoritesEntities(id: Int, login: String): Completable =
        favoritesTMDbDao.addEntityToFavoritesEntitiesTable(
            RoomFavoritesEntity(
                uniqueId = 0,
                id = id,
                login = login
            )
        )

    override fun deleteEntityFromFavoritesEntities(id: Int, login: String): Completable =
        favoritesTMDbDao.deleteEntityFromFavoritesEntitiesTable(id, login)


    override fun loadFavoritesMovieEntities(
        cinema: String,
        ids: List<Int>
    ): Single<List<RoomCinemaInfoDataEntity>> =
        favoritesTMDbDao.loadFavoriteCinemaEntitiesByIdArray(cinema, ids.toIntArray())

    override fun loadFavoritesTvSeriesEntities(
        cinema: String,
        ids: List<Int>
    ): Single<List<RoomCinemaInfoDataEntity>> =
        favoritesTMDbDao.loadFavoriteCinemaEntitiesByIdArray(cinema, ids.toIntArray())


}
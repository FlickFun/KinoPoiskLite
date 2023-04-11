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

import androidx.room.*
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.*
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.CINEMA_INFO_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.TC_CIE_CINEMA
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.TC_CIE_ID
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.TC_CIE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity.Companion.TC_CIE_TITLE
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity.Companion.FAVORITE_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity.Companion.TC_FE_ID
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.MoviePosterEntity.Companion.MOVIE_POSTER_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.MoviePosterEntity.Companion.TC_MPE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity.Companion.TC_TSPE_POPULARITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity.Companion.TV_SERIES_POSTER_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity.Companion.TC_UE_LOGGED
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity.Companion.TC_UE_LOGIN
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity.Companion.USER_ENTITY
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity.Companion.TC_FE_LOGIN
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TMDbDao {

    @Query("SELECT * FROM $MOVIE_POSTER_ENTITY ORDER BY $TC_MPE_POPULARITY DESC")
    suspend fun loadMoviePosterEntities(): List<MoviePosterEntity>

    @Insert(entity = MoviePosterEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoviePosterEntities(moviePopularListEntities: List<MoviePosterEntity>)


    @Query("SELECT * FROM $TV_SERIES_POSTER_ENTITY ORDER BY $TC_TSPE_POPULARITY DESC")
    fun loadTvSeriesPosterEntities(): Single<List<TvSeriesPosterEntity>>

    @Insert(entity = TvSeriesPosterEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveTvSeriesPosterEntities(tvSeriesPopularListEntities: List<TvSeriesPosterEntity>): Completable


    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_ID = :id")
    suspend fun loadCinemaInfoEntity(id: Int): CinemaInfoEntity

    @Insert(entity = CinemaInfoEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCinemaInfoEntity(cinemaInfoEntity: CinemaInfoEntity)


    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_TITLE LIKE '%' || :searchText || '%' ORDER BY $TC_CIE_POPULARITY DESC")
    suspend fun loadMovieEntityByText(cinema: String, searchText: String): List<CinemaInfoEntity>

    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_TITLE LIKE '%' || :searchText || '%' ORDER BY $TC_CIE_POPULARITY DESC")
    fun loadTvSeriesEntityByText(cinema: String, searchText: String): Single<List<CinemaInfoEntity>>


    @Query("SELECT $TC_FE_ID FROM $FAVORITE_ENTITY WHERE $TC_FE_LOGIN = :login")
    fun loadAllFavoriteEntitiesIdsByLogin(login: String): Single<List<Int>>

    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addEntityToFavoriteEntitiesTable(favoriteEntity: FavoriteEntity): Completable

    @Query("DELETE FROM $FAVORITE_ENTITY WHERE $TC_FE_ID = :id AND $TC_FE_LOGIN = :login")
    fun deleteEntityFromFavoriteEntitiesTable(id: Int, login: String): Completable


    @Query("SELECT * FROM $CINEMA_INFO_ENTITY WHERE $TC_CIE_CINEMA = :cinema AND $TC_CIE_ID IN (:ids) ORDER BY $TC_CIE_POPULARITY DESC")
    fun loadFavoriteCinemaEntitiesByIdArray(
        cinema: String,
        ids: IntArray
    ): Single<List<CinemaInfoEntity>>


    @Query("SELECT * FROM $USER_ENTITY WHERE $TC_UE_LOGGED = :logged")
    fun loadLoggedUserInfoEntity(logged: Boolean): Single<UserEntity>

    @Query("SELECT * FROM $USER_ENTITY WHERE $TC_UE_LOGIN = :login")
    fun loadUserInfoEntity(login: String): Single<UserEntity>

    @Insert(entity = UserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addEntityToUserEntityTable(userEntity: UserEntity): Completable


}
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
package com.fsoftstudio.kinopoisklite.domain.data

import com.fsoftstudio.kinopoisklite.data.request.remote.responses.*
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.models.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface DataRepository {
    suspend fun getLocalMoviePosters() : ResponseMoviesList
    suspend fun getRemoteMoviePosters() : Response<ResponseMoviesList>
    fun getLocalTvSeriesPosters() : Observable<ResponseTvSeriesList>
    fun getRemoteTvSeriesPosters() : Observable<ResponseTvSeriesList>

    suspend fun getLocalListMovieSearch(searchText: String) : Flow<ResponseMoviesList>
    suspend fun getRemoteListMovieSearch(searchText: String) : Response<ResponseMoviesList>
    fun getLocalListTvSeriesSearch(searchText: String) : Observable<ResponseTvSeriesList>
    fun getRemoteListTvSeriesSearch(searchText: String) : Observable<ResponseTvSeriesList>

    suspend fun getLocalCinemaInfo(id: Int, cinema: String) : CinemaInfo
    suspend fun saveLocalCinemaInfo(responseCinemaInfo: ResponseCinemaInfo, cinemaInfo: CinemaInfo, cinema: String)
    suspend fun getRemoteCinemaInfo(id: Int, cinema: String) : Response<ResponseCinemaInfo>
    suspend fun getRemoteCinemaActorsList(id: Int, cinema: String) : Response<ResponseCinemaActorsList>

    fun loadAllFavoriteEntitiesIdsByLogin(login: String): Single<List<Int>>
    fun addEntityIdToFavoriteEntities(id: Int, login: String): Completable
    fun deleteEntityIdFromFavoriteEntities(id: Int, login: String): Completable

    fun getFavoriteMoviePostersListByLogin(login: String) : Observable<List<Poster>>
    fun getFavoriteTvSeriesPostersListByLogin(login: String) : Observable<List<Poster>>

    fun getLoggedUserInfo() : Observable<User>
    fun getUserInfo(login: String) : Observable<User>
    fun saveUserInfo(user: User) : Completable

    suspend fun getAppData() : Flow<AppData?>

    fun getSavedTheme() : Int
    fun saveTheme(theme: Int)

    fun getTelegramDev() : String?
    fun saveTelegramDev(telegramDev: String)

    fun getBootAutoStart() : Int
    fun saveBootAutoStart(bootAutoStart: Int)
}
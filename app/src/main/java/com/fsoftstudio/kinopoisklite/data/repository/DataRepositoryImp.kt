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
package com.fsoftstudio.kinopoisklite.data.repository

import com.fsoftstudio.kinopoisklite.data.request.local.AppLocalDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.TMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.*
import com.fsoftstudio.kinopoisklite.data.request.remote.AppRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.TMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.*
import com.fsoftstudio.kinopoisklite.domain.data.DataRepository
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.models.User
import com.fsoftstudio.kinopoisklite.domain.models.mapToUserEntity
import com.fsoftstudio.kinopoisklite.parameters.Sys.ERROR_WRONG_CATEGORY
import com.fsoftstudio.kinopoisklite.parameters.Sys.MOVIE
import com.fsoftstudio.kinopoisklite.parameters.Sys.TV_SERIES
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class DataRepositoryImp @Inject constructor(
    private val tMDbLocalDataSource: TMDbLocalDataSource,
    private val tMDbRemoteDataSource: TMDbRemoteDataSource,
    private val appRemoteDataSource: AppRemoteDataSource,
    private val appLocalDataSource: AppLocalDataSource
) : DataRepository {


    override suspend fun getLocalMoviePosters(): ResponseMoviesList =
        ResponseMoviesList(
            results = tMDbLocalDataSource.loadMoviePopularEntities()
                .map { it.mapToRemote() }
        )

    override suspend fun getRemoteMoviePosters(): Response<ResponseMoviesList> =
        tMDbRemoteDataSource.getResponseMoviesList().also {
            tMDbLocalDataSource.saveListMoviesPosters(it.body()?.results?.take(9))
        }

    override fun getLocalTvSeriesPosters(): Observable<ResponseTvSeriesList> =
        tMDbLocalDataSource.loadTvSeriesPopularEntities().toObservable()
            .flatMap { entities ->
                Observable.just(
                    ResponseTvSeriesList(
                        results = entities.map { it.mapToRemote() }
                    )
                )
            }

    override fun getRemoteTvSeriesPosters(): Observable<ResponseTvSeriesList> =
        tMDbRemoteDataSource.getResponseTvSeriesList().toObservable()
            .flatMap {
                tMDbLocalDataSource.saveListTvSeriesPosters(it.results.take(9))
                    .andThen(Observable.just(it))
            }

    @OptIn(FlowPreview::class)
    override suspend fun getLocalListMovieSearch(searchText: String): Flow<ResponseMoviesList> {
        return tMDbLocalDataSource.searchMovieEntitiesByText(MOVIE, searchText).flatMapConcat {
            flow {
                emit(
                    ResponseMoviesList(
                        results = it.map { it.mapToResponseMoviePopular() }
                    ))
            }
        }
    }

    override suspend fun getRemoteListMovieSearch(searchText: String): Response<ResponseMoviesList> =
        tMDbRemoteDataSource.getResponseListMoviesSearchByText(searchText)

    override fun getLocalListTvSeriesSearch(searchText: String): Observable<ResponseTvSeriesList> =
        tMDbLocalDataSource.searchTvSeriesEntitiesByText(TV_SERIES, searchText)
            .toObservable()
            .flatMap { entities ->
                Observable.just(
                    ResponseTvSeriesList(
                        results = entities.map { it.mapToResponseTvSeriesPopular() }
                    )
                )
            }

    override fun getRemoteListTvSeriesSearch(searchText: String): Observable<ResponseTvSeriesList> =
        tMDbRemoteDataSource.getResponseListTvSeriesSearchByText(searchText)
            .toObservable()
            .flatMap {
                Observable.just(it)
            }

    override suspend fun getLocalCinemaInfo(id: Int, cinema: String): CinemaInfo =
        tMDbLocalDataSource.loadCinemaInfoEntity(id).mapToCinemaInfo()

    override suspend fun saveLocalCinemaInfo(
        responseCinemaInfo: ResponseCinemaInfo,
        cinemaInfo: CinemaInfo,
        cinema: String
    ) {
        tMDbLocalDataSource.saveCinemaInfoEntity(
            responseCinemaInfo.mapToEntity(cinemaInfo, cinema)
        )
    }

    override suspend fun getRemoteCinemaInfo(
        id: Int,
        cinema: String
    ): Response<ResponseCinemaInfo> {

        return when (cinema) {
            MOVIE -> {
                tMDbRemoteDataSource.getResponseMovieInfo(id)
            }

            TV_SERIES -> {
                tMDbRemoteDataSource.getResponseTvSeriesInfo(id)
            }

            else -> {
                Response.error(405, ERROR_WRONG_CATEGORY.toResponseBody())
            }
        }
    }


    override suspend fun getRemoteCinemaActorsList(
        id: Int,
        cinema: String
    ): Response<ResponseCinemaActorsList> {

        return when (cinema) {
            MOVIE -> {
                tMDbRemoteDataSource.getResponseMovieActorsList(id)
            }

            TV_SERIES -> {
                tMDbRemoteDataSource.getResponseTvSeriesActorsList(id)
            }
            else -> {
                Response.error(405, ERROR_WRONG_CATEGORY.toResponseBody())
            }
        }
    }

    override fun loadAllFavoriteEntitiesIdsByLogin(login: String): Single<List<Int>> =
        tMDbLocalDataSource.loadAllFavoriteEntitiesIdsByLogin(login)

    override fun addEntityIdToFavoriteEntities(id: Int, login: String): Completable =
        tMDbLocalDataSource.addEntityToFavoriteEntities(id, login)

    override fun deleteEntityIdFromFavoriteEntities(id: Int, login: String): Completable =
        tMDbLocalDataSource.deleteEntityFromFavoriteEntities(id, login)


    override fun getFavoriteMoviePostersListByLogin(login: String): Observable<List<Poster>> =
        loadAllFavoriteEntitiesIdsByLogin(login).toObservable()
            .flatMap { intList ->
                tMDbLocalDataSource.loadFavoriteMovieEntities(MOVIE, intList)
                    .toObservable()
                    .flatMap { entities ->
                        Observable.just(entities.map { it.mapToPoster() })
                    }
            }

    override fun getFavoriteTvSeriesPostersListByLogin(login: String): Observable<List<Poster>> =
        loadAllFavoriteEntitiesIdsByLogin(login).toObservable()
            .flatMap { intList ->
                tMDbLocalDataSource.loadFavoriteTvSeriesEntities(TV_SERIES, intList)
                    .toObservable()
                    .flatMap { entities ->
                        Observable.just(entities.map { it.mapToPoster() })
                    }
            }

    override fun getLoggedUserInfo(): Observable<User> =
        tMDbLocalDataSource.loadLoggedUserInfoEntity(true).toObservable()
            .flatMap {
                Observable.just(it.mapToUser())
            }

    override fun getUserInfo(login: String): Observable<User> =
        tMDbLocalDataSource.loadUserInfoEntity(login).toObservable()
            .flatMap {
                Observable.just(it.mapToUser())
            }

    override fun saveUserInfo(user: User) =
        tMDbLocalDataSource.saveUserInfoEntity(user.mapToUserEntity())

    override suspend fun getAppData(): Flow<AppData?> =
        appRemoteDataSource.getAppData()

    override fun getSavedTheme(): Int = appLocalDataSource.getSavedTheme()
    override fun saveTheme(theme: Int) = appLocalDataSource.saveTheme(theme)
    override fun getTelegramDev(): String? = appLocalDataSource.getTelegramDev()
    override fun saveTelegramDev(telegramDev: String) =
        appLocalDataSource.saveTelegramDev(telegramDev)

    override fun getBootAutoStart(): Int = appLocalDataSource.getBootAutoStart()
    override fun saveBootAutoStart(bootAutoStart: Int) =
        appLocalDataSource.saveBootAutoStart(bootAutoStart)

}
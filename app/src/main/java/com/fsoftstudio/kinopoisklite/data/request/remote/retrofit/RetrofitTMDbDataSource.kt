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
package com.fsoftstudio.kinopoisklite.data.request.remote.retrofit

import com.fsoftstudio.kinopoisklite.data.request.remote.TMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseCinemaActorsList
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseCinemaInfo
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseMoviesList
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseTvSeriesList
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class RetrofitTMDbDataSource(private val tMDbApi: TMDbApi) :
    TMDbRemoteDataSource {

    override suspend fun getResponseMoviesList(): Response<ResponseMoviesList> =
        tMDbApi.getListMoviesPosters(AppUseCase.apiKey)

    override fun getResponseTvSeriesList(): Single<ResponseTvSeriesList> =
        tMDbApi.getListTvSeriesPosters(AppUseCase.apiKey)


    override suspend fun getResponseListMoviesSearchByText(searchText: String): Response<ResponseMoviesList> =
        tMDbApi.getMovieForUserSearch(searchText, AppUseCase.apiKey)

    override fun getResponseListTvSeriesSearchByText(searchText: String): Single<ResponseTvSeriesList> =
        tMDbApi.getTvSeriesForUserSearch(searchText, AppUseCase.apiKey)


    override suspend fun getResponseMovieInfo(id: Int): Response<ResponseCinemaInfo> =
        tMDbApi.getResponseMovieInfo(id.toString(), AppUseCase.apiKey)

    override suspend fun getResponseTvSeriesInfo(id: Int): Response<ResponseCinemaInfo> =
        tMDbApi.getResponseTvSeriesInfo(id.toString(), AppUseCase.apiKey)


    override suspend fun getResponseMovieActorsList(id: Int): Response<ResponseCinemaActorsList> =
        tMDbApi.getResponseMovieActors(id.toString(), AppUseCase.apiKey)

    override suspend fun getResponseTvSeriesActorsList(id: Int): Response<ResponseCinemaActorsList> =
        tMDbApi.getResponseTvSeriesActors(id.toString(), AppUseCase.apiKey)
}
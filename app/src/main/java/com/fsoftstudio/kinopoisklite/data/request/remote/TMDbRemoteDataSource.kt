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
package com.fsoftstudio.kinopoisklite.data.request.remote

import com.fsoftstudio.kinopoisklite.data.request.remote.responses.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface TMDbRemoteDataSource {
    suspend fun getResponseMoviesList(): Response<ResponseMoviesList>
    fun getResponseTvSeriesList(): Single<ResponseTvSeriesList>

    suspend fun getResponseListMoviesSearchByText(searchText: String): Response<ResponseMoviesList>
    fun getResponseListTvSeriesSearchByText(searchText: String): Single<ResponseTvSeriesList>

    suspend fun getResponseMovieInfo(id: Int): Response<ResponseCinemaInfo>
    suspend fun getResponseTvSeriesInfo(id: Int): Response<ResponseCinemaInfo>

    suspend fun getResponseMovieActorsList(id: Int): Response<ResponseCinemaActorsList>
    suspend fun getResponseTvSeriesActorsList(id: Int): Response<ResponseCinemaActorsList>
}
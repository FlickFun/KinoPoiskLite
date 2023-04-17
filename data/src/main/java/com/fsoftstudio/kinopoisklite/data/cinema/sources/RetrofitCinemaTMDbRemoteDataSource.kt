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
package com.fsoftstudio.kinopoisklite.data.cinema.sources

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RetrofitCinemaActorDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RetrofitCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.settings.entity.ApiKey
import retrofit2.Response
import javax.inject.Inject

class RetrofitCinemaTMDbRemoteDataSource @Inject constructor(
    private val cinemaTMDbApi: CinemaTMDbApi
) : CinemaTMDbRemoteDataSource {

    override suspend fun getResponseMovieInfo(id: Int): Response<RetrofitCinemaInfoDataEntity> =
        cinemaTMDbApi.getResponseMovieInfo(id.toString(), ApiKey.getKey)

    override suspend fun getResponseTvSeriesInfo(id: Int): Response<RetrofitCinemaInfoDataEntity> =
        cinemaTMDbApi.getResponseTvSeriesInfo(id.toString(), ApiKey.getKey)


    override suspend fun getResponseMovieActorsList(id: Int): Response<RetrofitCinemaActorDataEntitiesList> =
        cinemaTMDbApi.getResponseMovieActors(id.toString(), ApiKey.getKey)

    override suspend fun getResponseTvSeriesActorsList(id: Int): Response<RetrofitCinemaActorDataEntitiesList> =
        cinemaTMDbApi.getResponseTvSeriesActors(id.toString(), ApiKey.getKey)
}
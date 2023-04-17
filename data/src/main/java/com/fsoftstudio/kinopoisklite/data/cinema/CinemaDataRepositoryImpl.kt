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
package com.fsoftstudio.kinopoisklite.data.cinema

import com.fsoftstudio.kinopoisklite.data.CinemaDataRepository
import com.fsoftstudio.kinopoisklite.data.cinema.entities.*
import com.fsoftstudio.kinopoisklite.data.cinema.sources.CinemaTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.cinema.sources.CinemaTMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.ERROR_WRONG_CATEGORY
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.MOVIE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.TV_SERIES
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Inject

class CinemaDataRepositoryImpl @Inject constructor(
    private val cinemaTMDbLocalDataSource: CinemaTMDbLocalDataSource,
    private val cinemaTMDbRemoteDataSource: CinemaTMDbRemoteDataSource
) : CinemaDataRepository {




    override suspend fun getLocalCinemaInfo(id: Int, cinema: String): RoomCinemaInfoDataEntity =
        cinemaTMDbLocalDataSource.loadCinemaInfoEntity(id)

    override suspend fun saveLocalCinemaInfo(roomCinemaInfoDataEntity: RoomCinemaInfoDataEntity) =
        cinemaTMDbLocalDataSource.saveCinemaInfoEntity(roomCinemaInfoDataEntity)

    override suspend fun getRemoteCinemaInfo(
        id: Int,
        cinema: String
    ): Response<RetrofitCinemaInfoDataEntity> {

        return when (cinema) {
            MOVIE -> {
                cinemaTMDbRemoteDataSource.getResponseMovieInfo(id)
            }

            TV_SERIES -> {
                cinemaTMDbRemoteDataSource.getResponseTvSeriesInfo(id)
            }

            else -> {
                Response.error(405, ERROR_WRONG_CATEGORY.toResponseBody())
            }
        }
    }


    override suspend fun getRemoteCinemaActorsList(
        id: Int,
        cinema: String
    ): Response<RetrofitCinemaActorDataEntitiesList> {

        return when (cinema) {
            MOVIE -> {
                cinemaTMDbRemoteDataSource.getResponseMovieActorsList(id)
            }

            TV_SERIES -> {
                cinemaTMDbRemoteDataSource.getResponseTvSeriesActorsList(id)
            }
            else -> {
                Response.error(405, ERROR_WRONG_CATEGORY.toResponseBody())
            }
        }
    }
}
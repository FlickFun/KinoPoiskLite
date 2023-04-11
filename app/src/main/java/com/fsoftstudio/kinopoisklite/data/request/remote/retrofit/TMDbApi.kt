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

import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseCinemaActorsList
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseCinemaInfo
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseMoviesList
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.ResponseTvSeriesList
import com.fsoftstudio.kinopoisklite.parameters.Sys.API_KEY
import com.fsoftstudio.kinopoisklite.parameters.Sys.APPEND_TO_RESPONSE
import com.fsoftstudio.kinopoisklite.parameters.Sys.APPEND_TO_RESPONSE_VALUE
import com.fsoftstudio.kinopoisklite.parameters.Sys.APPLICATION_JSON
import com.fsoftstudio.kinopoisklite.parameters.Sys.CREDITS_ACTORS
import com.fsoftstudio.kinopoisklite.parameters.Sys.INCLUDE_IMAGE_LANGUAGE
import com.fsoftstudio.kinopoisklite.parameters.Sys.INCLUDE_IMAGE_LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.parameters.Sys.LANGUAGE
import com.fsoftstudio.kinopoisklite.parameters.Sys.LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.parameters.Sys.MOVIE_INFO
import com.fsoftstudio.kinopoisklite.parameters.Sys.MOVIE_POPULAR
import com.fsoftstudio.kinopoisklite.parameters.Sys.QUERY
import com.fsoftstudio.kinopoisklite.parameters.Sys.SEARCH_MOVIE
import com.fsoftstudio.kinopoisklite.parameters.Sys.SEARCH_TV_SERIES
import com.fsoftstudio.kinopoisklite.parameters.Sys.TV_SERIES_INFO
import com.fsoftstudio.kinopoisklite.parameters.Sys.TV_SERIES_POPULAR
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDbApi {

    @GET(MOVIE_POPULAR)
    @Headers(APPLICATION_JSON)
    suspend fun getListMoviesPosters(
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseMoviesList>

    @GET(TV_SERIES_POPULAR)
    @Headers(APPLICATION_JSON)
    fun getListTvSeriesPosters(
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Single<ResponseTvSeriesList>

    @GET(SEARCH_MOVIE)
    suspend fun getMovieForUserSearch(
        @Query(value = QUERY) searchText: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseMoviesList>

    @GET(SEARCH_TV_SERIES)
    fun getTvSeriesForUserSearch(
        @Query(QUERY) searchText: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Single<ResponseTvSeriesList>

    @GET("${MOVIE_INFO}{id}")
    suspend fun getResponseMovieInfo(
        @Path("id") id: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseCinemaInfo>


    @GET("${TV_SERIES_INFO}{id}")
    suspend fun getResponseTvSeriesInfo(
        @Path("id") id: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseCinemaInfo>


    @GET("${MOVIE_INFO}{id}${CREDITS_ACTORS}")
    suspend fun getResponseMovieActors(
        @Path("id") id: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseCinemaActorsList>

    @GET("${TV_SERIES_INFO}{id}${CREDITS_ACTORS}")
    suspend fun getResponseTvSeriesActors(
        @Path("id") id: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<ResponseCinemaActorsList>
}
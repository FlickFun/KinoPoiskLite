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
package com.fsoftstudio.kinopoisklite.data.movies.sources

import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMovieDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.API_KEY
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.APPEND_TO_RESPONSE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.APPEND_TO_RESPONSE_VALUE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.APPLICATION_JSON
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.INCLUDE_IMAGE_LANGUAGE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.INCLUDE_IMAGE_LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.LANGUAGE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.MOVIE_POPULAR
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.QUERY
import com.fsoftstudio.kinopoisklite.data.parameters.ConstData.SEARCH_MOVIE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface MoviesTMDbApi {
    @GET(MOVIE_POPULAR)
    @Headers(APPLICATION_JSON)
    suspend fun getListMoviesPosters(
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<RetrofitMovieDataEntitiesList>

    @GET(SEARCH_MOVIE)
    suspend fun getMovieForUserSearch(
        @Query(value = QUERY) searchText: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Response<RetrofitMovieDataEntitiesList>
}
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
package com.fsoftstudio.kinopoisklite.data.tvseries.sources

import com.fsoftstudio.kinopoisklite.common.entity.Const.API_KEY
import com.fsoftstudio.kinopoisklite.common.entity.Const.APPEND_TO_RESPONSE
import com.fsoftstudio.kinopoisklite.common.entity.Const.APPEND_TO_RESPONSE_VALUE
import com.fsoftstudio.kinopoisklite.common.entity.Const.APPLICATION_JSON
import com.fsoftstudio.kinopoisklite.common.entity.Const.INCLUDE_IMAGE_LANGUAGE
import com.fsoftstudio.kinopoisklite.common.entity.Const.INCLUDE_IMAGE_LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.common.entity.Const.LANGUAGE
import com.fsoftstudio.kinopoisklite.common.entity.Const.LANGUAGE_VALUE
import com.fsoftstudio.kinopoisklite.common.entity.Const.QUERY
import com.fsoftstudio.kinopoisklite.common.entity.Const.SEARCH_TV_SERIES
import com.fsoftstudio.kinopoisklite.common.entity.Const.TV_SERIES_POPULAR
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesDataEntitiesList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface TvSeriesTMDbApi {

    @GET(TV_SERIES_POPULAR)
    @Headers(APPLICATION_JSON)
    fun getListTvSeriesPosters(
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Single<RetrofitTvSeriesDataEntitiesList>

    @GET(SEARCH_TV_SERIES)
    fun getTvSeriesForUserSearch(
        @Query(QUERY) searchText: String,
        @Query(API_KEY) apiKey: String,
        @Query(LANGUAGE) language: String = LANGUAGE_VALUE,
        @Query(APPEND_TO_RESPONSE) append_to_response: String = APPEND_TO_RESPONSE_VALUE,
        @Query(INCLUDE_IMAGE_LANGUAGE) include_image_language: String = INCLUDE_IMAGE_LANGUAGE_VALUE
    ): Single<RetrofitTvSeriesDataEntitiesList>
}
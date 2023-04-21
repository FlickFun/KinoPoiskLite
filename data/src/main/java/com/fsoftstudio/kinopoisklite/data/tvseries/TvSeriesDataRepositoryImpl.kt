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
package com.fsoftstudio.kinopoisklite.data.tvseries

import com.fsoftstudio.kinopoisklite.data.TvSeriesDataRepository
import com.fsoftstudio.kinopoisklite.data.cinema.entities.mapToRetrofitTvSeriesPosterDataEntity
import com.fsoftstudio.kinopoisklite.common.entity.Const.TV_SERIES
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.mapToRemote
import com.fsoftstudio.kinopoisklite.data.tvseries.sources.TvSeriesTMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.tvseries.sources.TvSeriesTMDbLocalDataSource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class TvSeriesDataRepositoryImpl @Inject constructor(
    private val tvSeriesTMDbLocalDataSource: TvSeriesTMDbLocalDataSource,
    private val tvSeriesTMDbRemoteDataSource: TvSeriesTMDbRemoteDataSource
) : TvSeriesDataRepository {


    override fun getLocalTvSeriesPosters(): Observable<RetrofitTvSeriesDataEntitiesList> =
        tvSeriesTMDbLocalDataSource.loadTvSeriesPopularEntities().toObservable()
            .flatMap { entities ->
                Observable.just(
                    RetrofitTvSeriesDataEntitiesList(
                        results = entities.take(9).map { it.mapToRemote() }
                    )
                )
            }

    override fun getRemoteTvSeriesPosters(): Observable<RetrofitTvSeriesDataEntitiesList> =
        tvSeriesTMDbRemoteDataSource.getResponseTvSeriesList().toObservable()
            .flatMap {
                val retrofitTvSeriesDataEntitiesList =
                    RetrofitTvSeriesDataEntitiesList(results = it.results.take(9))
                tvSeriesTMDbLocalDataSource.saveListTvSeriesPosters(retrofitTvSeriesDataEntitiesList.results)
                    .andThen(Observable.just(retrofitTvSeriesDataEntitiesList))
            }

    override fun getLocalListTvSeriesSearch(searchText: String): Observable<RetrofitTvSeriesDataEntitiesList> =
        tvSeriesTMDbLocalDataSource.searchTvSeriesEntitiesByText(TV_SERIES, searchText)
            .toObservable()
            .flatMap { entities ->
                Observable.just(
                    RetrofitTvSeriesDataEntitiesList(
                        results = entities.map { it.mapToRetrofitTvSeriesPosterDataEntity() }
                    )
                )
            }

    override fun getRemoteListTvSeriesSearch(searchText: String): Observable<RetrofitTvSeriesDataEntitiesList> =
        tvSeriesTMDbRemoteDataSource.getResponseListTvSeriesSearchByText(searchText)
            .toObservable()
            .flatMap {
                Observable.just(it)
            }
}
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

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesPosterDataEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.mapToLocal
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RoomTvSeriesPosterEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RoomTvSeriesTMDbLocalDataSource (
    private val tvSeriesTMDbDao: TvSeriesTMDbDao
) : TvSeriesTMDbLocalDataSource {

    override fun loadTvSeriesPopularEntities(): Single<List<RoomTvSeriesPosterEntity>> =
        tvSeriesTMDbDao.loadTvSeriesPosterEntities()

    override fun saveListTvSeriesPosters(listRetrofitTvSeriesPosterDataEntity: List<RetrofitTvSeriesPosterDataEntity>): Completable =
        tvSeriesTMDbDao.saveTvSeriesPosterEntities(
            listRetrofitTvSeriesPosterDataEntity.map { tvSeriesPoster -> tvSeriesPoster.mapToLocal() }
        )

    override fun searchTvSeriesEntitiesByText(
        cinema: String,
        searchText: String
    ): Single<List<RoomCinemaInfoDataEntity>> = tvSeriesTMDbDao.loadTvSeriesEntityByText(cinema, searchText)

    override fun deleteTvSeriesPopularEntities() {
        tvSeriesTMDbDao.delete(tvSeriesTMDbDao.loadTvSeriesPosterEntities().blockingGet())

    }

}
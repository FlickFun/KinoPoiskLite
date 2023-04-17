package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesDataEntitiesList
import io.reactivex.rxjava3.core.Observable

interface TvSeriesDataRepository {
    fun getLocalTvSeriesPosters() : Observable<RetrofitTvSeriesDataEntitiesList>
    fun getRemoteTvSeriesPosters() : Observable<RetrofitTvSeriesDataEntitiesList>

    fun getLocalListTvSeriesSearch(searchText: String) : Observable<RetrofitTvSeriesDataEntitiesList>
    fun getRemoteListTvSeriesSearch(searchText: String) : Observable<RetrofitTvSeriesDataEntitiesList>

}
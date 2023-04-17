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
package com.fsoftstudio.kinopoisklite.domain.usecase

import com.fsoftstudio.kinopoisklite.data.MoviesDataRepository
import com.fsoftstudio.kinopoisklite.data.TvSeriesDataRepository
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMovieDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesDataEntitiesList
import com.fsoftstudio.kinopoisklite.domain.mappers.PosterMapper
import com.fsoftstudio.kinopoisklite.domain.ui.UiListCinemaSearch
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase.Companion.EXCEPTION_LIST_CINEMA_SEARCH
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase.Companion.EXCEPTION_POSTERS
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinema
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinemaImp
import com.fsoftstudio.kinopoisklite.ui.screens.search.SearchViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import okhttp3.ResponseBody
import javax.inject.Inject

class ListCinemaSearchUseCase @Inject constructor(
    private val favoriteCinemaImp: FavoriteCinemaImp,
    private val cinemaInfoUseCase: CinemaInfoUseCase,
    private val moviesDataRepository: MoviesDataRepository,
    private val tvSeriesDataRepository: TvSeriesDataRepository,
    private val posterMapper: PosterMapper,
    private val uiListCinemaSearch: UiListCinemaSearch,
    private val exceptionsUseCase: ExceptionsUseCase
) : FavoriteCinema {

    private var searchViewModel: SearchViewModel? = null

    fun searchCinema(
        compositeDisposable: CompositeDisposable,
        searchText: String,
        searchViewModel: SearchViewModel
    ) {
        this.searchViewModel = searchViewModel
        getMovie(searchText)
        getTvSeries(compositeDisposable, searchText)
    }

    private fun getMovie(searchText: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        }
        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            moviesDataRepository.getLocalListMovieSearch(searchText).collectLatest {
                if (it.results.isNotEmpty()) {
                    sendShowMovie(it)
                    updateMovies(searchText, false)
                } else {
                    updateMovies(searchText, true)
                }
            }
        }
    }

    private fun updateMovies(
        searchText: String,
        isNoHasData: Boolean
    ) {
        fun update() {
            val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
                exceptionsUseCase.showNoInternet(EXCEPTION_POSTERS)
                if (isNoHasData) {
                    sendShowNoMovieData()
                }
            }
            movie = CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                val response = moviesDataRepository.getRemoteListMovieSearch(searchText)
                if (response.isSuccessful) {

                    if (response.body()?.results?.isNotEmpty() == true) {
                        sendShowMovie(response.body()!!)
                    } else if (isNoHasData) {
                        sendShowNoMovieData()
                    }

                } else {
                    if (isNoHasData) {
                        sendShowNoMovieData()
                    }
                    exceptionsUseCase.showHttpExceptionInfo(
                        EXCEPTION_LIST_CINEMA_SEARCH,
                        response.code(),
                        (response.errorBody() as ResponseBody).string()
                    )
                }
            }
        }

        if (movie?.isActive == true) {
            movie?.cancel()
        }
        update()
    }

    private fun sendShowMovie(responseMoviesList: RetrofitMovieDataEntitiesList) =
        uiListCinemaSearch.showListMovie(
            posterMapper.fromRetrofitMoviePosterDataEntityList(responseMoviesList.results),
            searchViewModel!!
        )

    private fun sendShowNoMovieData() = uiListCinemaSearch.showMovieNoData(searchViewModel!!)

    private fun getTvSeries(compositeDisposable: CompositeDisposable, searchText: String) =
        compositeDisposable.add(
            tvSeriesDataRepository.getLocalListTvSeriesSearch(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isNotEmpty()) {
                        sendShowTvSeries(it)
                        updateTvSeries(compositeDisposable, searchText, false)
                    } else {
                        updateTvSeries(compositeDisposable, searchText, true)
                    }
                }, {
                    updateTvSeries(compositeDisposable, searchText, true)
                })
        )

    private fun updateTvSeries(
        compositeDisposable: CompositeDisposable,
        searchText: String,
        isNoHasData: Boolean
    ) =
        compositeDisposable.add(
            tvSeriesDataRepository.getRemoteListTvSeriesSearch(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isNotEmpty()) {
                        sendShowTvSeries(it)
                    } else {
                        if (isNoHasData) {
                            sendShowTvSeriesNoData()
                        }
                    }
                }, {
                    if (isNoHasData) {
                        sendShowTvSeriesNoData()
                    }
                })
        )

    private fun sendShowTvSeries(responseTvSeriesList: RetrofitTvSeriesDataEntitiesList) {
        uiListCinemaSearch.showListTvSeriesList(
            posterMapper.fromRetrofitTvSeriesPosterDataEntityList(responseTvSeriesList.results),
            searchViewModel!!
        )
    }

    private fun sendShowTvSeriesNoData() = uiListCinemaSearch.showTvSeriesNoData(searchViewModel!!)

    fun favoriteChecked(id: Int, cinema: String) {
        cinemaInfoUseCase.getCinemaInfo(id, cinema, false)
    }

    override fun addFavoritesCinemaToFavoritesList(id: Int) {
        favoriteCinemaImp.addFavoritesCinemaToFavoritesList(id)
    }

    override fun deleteFavoritesCinemaFromFavoritesList(id: Int) {
        favoriteCinemaImp.deleteFavoritesCinemaFromFavoritesList(id)
    }

    companion object {
        @JvmStatic
        var movie: Job? = null
    }
}
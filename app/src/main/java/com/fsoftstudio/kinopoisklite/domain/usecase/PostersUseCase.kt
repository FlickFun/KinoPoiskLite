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

import android.util.Log
import com.fsoftstudio.kinopoisklite.data.MoviesDataRepository
import com.fsoftstudio.kinopoisklite.data.TvSeriesDataRepository
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMovieDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesDataEntitiesList
import com.fsoftstudio.kinopoisklite.domain.mappers.PosterMapper
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.ui.UiPosters
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase.Companion.EXCEPTION_POSTERS
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinema
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinemaImp
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.TAG_MOVIE_BASE
import com.fsoftstudio.kinopoisklite.ui.screens.home.HomeFlowViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PostersUseCase @Inject constructor(
    private val favoriteCinemaImp: FavoriteCinemaImp,
    private val moviesDataRepository: MoviesDataRepository,
    private val tvSeriesDataRepository: TvSeriesDataRepository,
    private val posterMapper: PosterMapper,
    private val uiPoster: UiPosters,
    private val cinemaInfoUseCase: CinemaInfoUseCase,
    private val exceptionsUseCase: ExceptionsUseCase
) : FavoriteCinema {

    private var updateMoviePostersJob: Job? = null
    private var homeFlowViewModel: HomeFlowViewModel? = null
    private var isNotNotifyCheckChangePostersWorker = true
    private var moviePosters: List<Poster>? = null
    private var tvSeriesPosters: List<Poster>? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var isNeedNotificationThatDataChanged = false

    @Volatile
    private var isCanUpdate = true

    @Volatile
    private var isMovieNotUpdated = true

    @Volatile
    private var isTvSeriesNotUpdated = true

    fun readyToShowPosters(
        isCanUpdate: Boolean = true,
        compositeDisposable: CompositeDisposable,
        homeFlowViewModel: HomeFlowViewModel
    ) {
        this.isCanUpdate = isCanUpdate
        this.compositeDisposable = compositeDisposable
        this.homeFlowViewModel = homeFlowViewModel
        isNotNotifyCheckChangePostersWorker = true
        getMoviePosters()
        getTvSeriesPosters()
    }

    suspend fun checkChangePoster(callback: (Boolean) -> Unit) {
        moviePosters = null
        tvSeriesPosters = null
        isNotNotifyCheckChangePostersWorker = false
        isNeedNotificationThatDataChanged = false
        isCanUpdate = false
        compositeDisposable = CompositeDisposable()
        getMoviePosters()
        getTvSeriesPosters()
        var count = 0
        while (isTvSeriesNotUpdated || isMovieNotUpdated) {
            delay(1_000L)
            if (count++ > 10) {
                Log.i(
                    TAG_MOVIE_BASE,
                    "Posters not checked -> isMovieNotUpdated = $isMovieNotUpdated, isTvSeriesNotUpdated = $isTvSeriesNotUpdated ->"
                )
                break
            }
        }
        if (updateMoviePostersJob?.isActive == true) {
            updateMoviePostersJob?.cancel()
        }
        compositeDisposable?.dispose()
        callback.invoke(isNeedNotificationThatDataChanged)
    }

    private fun getMoviePosters() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            checkIsCanUpdateMovieAndUpdate()
            Log.i(
                TAG_MOVIE_BASE,
                "Error getLocalMoviePosters -> $e ->"
            )
        }
        CoroutineScope(IO).launch(coroutineExceptionHandler) {
            val result = moviesDataRepository.getLocalMoviePosters()
            sendShowMoviePosters(result)
            if (isCanUpdate) {
                updateMoviePosters()
            } else {
                checkIsCanUpdateMovieAndUpdate()
            }
        }
    }

    private fun checkIsCanUpdateMovieAndUpdate() {
        CoroutineScope(IO).launch {
            var count = 0
            while (!AppUseCase.isCanUpdate) {
                if (count++ == 10) {
                    exceptionsUseCase.showNoInternet(EXCEPTION_POSTERS)
                }
                delay(500)
            }
            isCanUpdate = true
            updateMoviePosters()
        }
    }

    private fun updateMoviePosters(isNeedDelay: Boolean = false) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            if (isNotNotifyCheckChangePostersWorker) {
                exceptionsUseCase.showNoInternet(EXCEPTION_POSTERS)
            }
            Log.i(
                TAG_MOVIE_BASE,
                "Error getRemoteMoviePosters -> $e ->"
            )
            updateMoviePosters(true)
        }
        updateMoviePostersJob = CoroutineScope(IO).launch(coroutineExceptionHandler) {
            if (isNeedDelay) {
                delay(3_000L)
            }
            val response = moviesDataRepository.getRemoteMoviePosters()

            if (response.isSuccessful) {
                response.body()?.results?.let {
                    sendShowMoviePosters(
                        RetrofitMovieDataEntitiesList(results = it.take(9))
                    )
                }
                isMovieNotUpdated = false

            } else {
                if (isNotNotifyCheckChangePostersWorker) {
                    exceptionsUseCase.showHttpExceptionInfo(
                        EXCEPTION_POSTERS,
                        response.code(),
                        (response.errorBody() as ResponseBody).string()
                    )
                }
            }
        }
    }

    private fun sendShowMoviePosters(
        responseMoviesList: RetrofitMovieDataEntitiesList?
    ) {
        responseMoviesList?.let {
            val postersList = posterMapper.fromRetrofitMoviePosterDataEntityList(it.results)
            if (postersList.isNotEmpty() && isNotSameList(postersList, moviePosters)) {
                if (isNotNotifyCheckChangePostersWorker) {
                    uiPoster.showPostersMovie(
                        postersList,
                        homeFlowViewModel!!
                    )
                } else {
                    moviePosters?.let { setIsNeedShowNotification() }
                }
            }
            moviePosters = postersList
            getCinemaInfoForAllPosters(postersList)
        }
    }


    private fun getTvSeriesPosters() =
        compositeDisposable!!.add(
            tvSeriesDataRepository.getLocalTvSeriesPosters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sendShowTvSeriesPosters(it)
                    checkIsCanUpdateTvSeriesAndUpdate()
                }, {
                    checkIsCanUpdateTvSeriesAndUpdate()
                })
        )

    private fun checkIsCanUpdateTvSeriesAndUpdate() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        }
        CoroutineScope(IO).launch(coroutineExceptionHandler) {
            if (isCanUpdate) {
                updateTvSeriesPosters()
            } else {
                while (!isCanUpdate) {
                    delay(500L)
                }
                updateTvSeriesPosters()
            }
        }
    }

    private fun updateTvSeriesPosters(delaySeconds: Long = 0) =
        compositeDisposable!!.add(
            tvSeriesDataRepository.getRemoteTvSeriesPosters()
                .delaySubscription(delaySeconds, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sendShowTvSeriesPosters(it)
                    isTvSeriesNotUpdated = false
                }, {
                    Log.i(
                        TAG_MOVIE_BASE,
                        "Error getRemoteTvSeriesPosters -> $it ->"
                    )
                    tryUpdateTvSeriesPostersAgain()
                })
        )

    private fun tryUpdateTvSeriesPostersAgain() {
        updateTvSeriesPosters(3)
    }

    private fun sendShowTvSeriesPosters(
        retrofitTvSeriesDataEntitiesList: RetrofitTvSeriesDataEntitiesList
    ) {
        val postersList = posterMapper.fromRetrofitTvSeriesPosterDataEntityList(
            retrofitTvSeriesDataEntitiesList.results
        )
        if (postersList.isNotEmpty() && isNotSameList(postersList, tvSeriesPosters)) {
            if (isNotNotifyCheckChangePostersWorker) {
                uiPoster.showPostersTvSeries(
                    postersList,
                    homeFlowViewModel!!
                )
            } else {
                tvSeriesPosters?.let { setIsNeedShowNotification() }
            }
        }
        tvSeriesPosters = postersList
        getCinemaInfoForAllPosters(postersList)
    }

    private fun isNotSameList(postersListNew: List<Poster>, postersList: List<Poster>?): Boolean {
        return if (postersList?.isNotEmpty() == true) {
            !postersList.all { e -> postersListNew.any { p -> e == p } }
        } else true
    }

    private fun getCinemaInfoForAllPosters(
        postersList: List<Poster>
    ) {
        CoroutineScope(IO).launch {
            var isNeedGetCinemaInfo = true
            var count = 0
            while (isTvSeriesNotUpdated || isMovieNotUpdated) {
                delay(1_000)
                if (count++ > 20) {
                    isNeedGetCinemaInfo = false
                    if (isNotNotifyCheckChangePostersWorker) {
                        exceptionsUseCase.showNoInternet(EXCEPTION_POSTERS)
                    }
                    break
                }
            }
            if (isNeedGetCinemaInfo) {
                postersList.forEach { poster ->
                    cinemaInfoUseCase.getCinemaInfo(
                        poster.id, poster.cinema, false
                    )
                }
            }
        }
    }

    private fun setIsNeedShowNotification() {
        isNeedNotificationThatDataChanged = true
    }

    override fun addFavoritesCinemaToFavoritesList(id: Int) {
        favoriteCinemaImp.addFavoritesCinemaToFavoritesList(id)
    }

    override fun deleteFavoritesCinemaFromFavoritesList(id: Int) {
        favoriteCinemaImp.deleteFavoritesCinemaFromFavoritesList(id)
    }
}
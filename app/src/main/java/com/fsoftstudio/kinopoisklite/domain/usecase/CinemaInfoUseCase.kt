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

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fsoftstudio.kinopoisklite.data.request.remote.responses.mapToCinemaInfo
import com.fsoftstudio.kinopoisklite.domain.data.DataRepository
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.ui.UiCinemaInfo
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase.Companion.EXCEPTION_CINEMA_INFO
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinema
import com.fsoftstudio.kinopoisklite.domain.usecase.inner.FavoriteCinemaImp
import com.fsoftstudio.kinopoisklite.parameters.Sys
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.ui.screens.cinema.CinemaInfoActivity
import com.fsoftstudio.kinopoisklite.ui.screens.cinema.CinemaViewModel
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import javax.inject.Inject


class CinemaInfoUseCase @Inject constructor(
    private val favoriteCinemaImp: FavoriteCinemaImp,
    private val dataRepository: DataRepository,
    private val uiCinemaInfo: UiCinemaInfo,
    private val exceptionsUseCase: ExceptionsUseCase
) : FavoriteCinema {

    private var cinemaViewModel: CinemaViewModel? = null

    fun needGetCinemaInfo(
        id: Int,
        cinema: String,
        cinemaViewModel: CinemaViewModel
    ) {
        this.cinemaViewModel = cinemaViewModel
        getCinemaInfo(id, cinema)
    }

    fun getCinemaInfo(
        id: Int,
        cinema: String,
        isNeedUpdateIfCalledFromCinemaInfoActivityAndShow: Boolean = true
    ) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            updateCinemaInfo(id, cinema, isNeedUpdateIfCalledFromCinemaInfoActivityAndShow)
        }
        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
            val cinemaInfo = dataRepository.getLocalCinemaInfo(id, cinema)
            if (isNeedUpdateIfCalledFromCinemaInfoActivityAndShow) {
                sendShowCinemaInfo(cinemaInfo)
                updateCinemaInfo(id, cinema, isNeedUpdateIfCalledFromCinemaInfoActivityAndShow)
            }
        }
    }

    private fun updateCinemaInfo(id: Int, cinema: String, needShow: Boolean) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            exceptionsUseCase.showNoInternet(EXCEPTION_CINEMA_INFO, cinemaViewModel)
        }
        CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {

            val responseCinemaInfo = async { dataRepository.getRemoteCinemaInfo(id, cinema) }
            if (responseCinemaInfo.await().isSuccessful) {
                responseCinemaInfo.await().body()?.let {
                    val responseCinemaActorsList =
                        async { dataRepository.getRemoteCinemaActorsList(id, cinema) }
                    if (responseCinemaActorsList.await().isSuccessful) {

                        val cinemaInfo = responseCinemaInfo.await().body()!!
                            .mapToCinemaInfo(responseCinemaActorsList.await().body())
                        dataRepository.saveLocalCinemaInfo(
                            responseCinemaInfo.await().body()!!,
                            cinemaInfo,
                            cinema
                        )

                        if (needShow) {
                            sendShowCinemaInfo(cinemaInfo)
                        }
                    } else {
                        exceptionsUseCase.showHttpExceptionInfo(
                            EXCEPTION_CINEMA_INFO,
                            responseCinemaActorsList.await().code(),
                            (responseCinemaActorsList.await().errorBody() as ResponseBody).string(),
                            cinemaViewModel
                        )
                    }
                }
            } else {
                exceptionsUseCase.showHttpExceptionInfo(
                    EXCEPTION_CINEMA_INFO,
                    responseCinemaInfo.await().code(),
                    (responseCinemaInfo.await().errorBody() as ResponseBody).string(),
                    cinemaViewModel
                )
            }
        }
    }

    fun openCinemaInfo(poster: Poster, ma: MainActivity) {
        val intent = Intent(ma, CinemaInfoActivity::class.java)
        intent.putExtra(Sys.ID_INT, poster.id ?: 0)
        intent.putExtra(Sys.TITLE, poster.title ?: Sys.NO_DATA)
        intent.putExtra(Sys.STAR_BOOLEAN, poster.favorite)
        intent.putExtra(Sys.CINEMA, poster.cinema)
        startActivity(ma, intent, null)
    }

    private fun sendShowCinemaInfo(cinemaInfo: CinemaInfo?) =
        cinemaInfo?.let {
            uiCinemaInfo.showCinemaInfo(it, cinemaViewModel!!)
        }


    override fun addFavoriteCinemaToFavoritesList(id: Int) {
        favoriteCinemaImp.addFavoriteCinemaToFavoritesList(id)
    }

    override fun deleteFavoriteCinemaFromFavoritesList(id: Int) {
        favoriteCinemaImp.deleteFavoriteCinemaFromFavoritesList(id)
    }
}
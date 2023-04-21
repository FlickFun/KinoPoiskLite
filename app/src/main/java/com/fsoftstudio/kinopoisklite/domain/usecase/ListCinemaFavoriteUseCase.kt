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

import com.fsoftstudio.kinopoisklite.data.FavoritesDataRepository
import com.fsoftstudio.kinopoisklite.domain.mappers.PosterMapper
import com.fsoftstudio.kinopoisklite.domain.ui.UiListCinemaFavorite
import com.fsoftstudio.kinopoisklite.ui.screens.favorite.FavoriteViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class ListCinemaFavoriteUseCase @Inject constructor(
    private val favoritesDataRepository: FavoritesDataRepository,
    private val posterMapper: PosterMapper,
    private val uiListCinemaFavorite: UiListCinemaFavorite,
    private val userProfileUseCase: UserProfileUseCase
) {

    private var favoriteViewModel: FavoriteViewModel? = null


    fun readyToShowFavorite(
        compositeDisposable: CompositeDisposable,
        favoriteViewModel: FavoriteViewModel
    ) {
        this.favoriteViewModel = favoriteViewModel
        movie(compositeDisposable)
        tvSeries(compositeDisposable)
    }

    private fun movie(compositeDisposable: CompositeDisposable) =
        compositeDisposable.add(
            favoritesDataRepository.getFavoritesMoviePostersListByLogin(userProfileUseCase.getLogin())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uiListCinemaFavorite.showFavoriteMovie(
                        posterMapper.fromRoomCinemaInfoDataEntityListForFavorites(it),
                        favoriteViewModel!!
                    )
                }, {

                })
        )

    private fun tvSeries(compositeDisposable: CompositeDisposable) =
        compositeDisposable.add(
            favoritesDataRepository.getFavoritesTvSeriesPostersListByLogin(userProfileUseCase.getLogin())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uiListCinemaFavorite.showFavoriteTvSeries(
                        posterMapper.fromRoomCinemaInfoDataEntityListForFavorites(it),
                        favoriteViewModel!!
                    )
                }, {

                })
        )

}
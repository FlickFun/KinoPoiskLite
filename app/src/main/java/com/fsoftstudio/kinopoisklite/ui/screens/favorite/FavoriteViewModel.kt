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
package com.fsoftstudio.kinopoisklite.ui.screens.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val listCinemaFavoriteUseCase: ListCinemaFavoriteUseCase
) : ViewModel() {

    private val _moviePosters = MutableLiveData<List<Poster>>()
    var moviePosters: LiveData<List<Poster>> = _moviePosters

    private var _tvSeriesPosters = MutableLiveData<List<Poster>>()
    var tvSeriesPosters: LiveData<List<Poster>> = _tvSeriesPosters

    val compositeDisposable = CompositeDisposable()

    fun sendToDomainThatReadyToShowFavorite() =
        listCinemaFavoriteUseCase.readyToShowFavorite(compositeDisposable, this)

    fun setMovieToLifeData(moviePosters: List<Poster>) = _moviePosters.postValue(moviePosters)
    fun setTvSeriesToLifeData(tvSeriesPosters: List<Poster>) =
        _tvSeriesPosters.postValue(tvSeriesPosters)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
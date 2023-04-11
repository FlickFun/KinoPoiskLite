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
package com.fsoftstudio.kinopoisklite.ui.imp

import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.ui.UiListCinemaSearch
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivityViewModel
import com.fsoftstudio.kinopoisklite.ui.screens.search.SearchViewModel
import javax.inject.Inject

class UiListCinemaSearchImp @Inject constructor() : UiListCinemaSearch {

    override fun showListMovie(
        moviePosters: List<Poster>,
        searchViewModel: SearchViewModel
    ) = searchViewModel.setMoviePostersToLiveData(moviePosters)

    override fun showMovieNoData(searchViewModel: SearchViewModel) =
        searchViewModel.setMoviePostersToLiveData(mutableListOf())

    override fun showListTvSeriesList(
        tvSeriesPosters: List<Poster>,
        searchViewModel: SearchViewModel
    ) = searchViewModel.setTvSeriesPostersToLiveData(tvSeriesPosters)

    override fun showTvSeriesNoData(searchViewModel: SearchViewModel) =
        searchViewModel.setTvSeriesPostersToLiveData(mutableListOf())

    override fun showException(exception: String, mainActivityViewModel: MainActivityViewModel) {
        mainActivityViewModel.setExceptionToStateFlow(exception)
    }
}

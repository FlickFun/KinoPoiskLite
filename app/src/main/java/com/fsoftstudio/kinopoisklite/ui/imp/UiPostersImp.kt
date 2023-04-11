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
import com.fsoftstudio.kinopoisklite.domain.ui.UiPosters
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivityViewModel
import com.fsoftstudio.kinopoisklite.ui.screens.home.HomeFlowViewModel
import javax.inject.Inject

class UiPostersImp @Inject constructor() : UiPosters {

    override fun showPostersMovie(
        moviePosters: List<Poster>,
        homeFlowViewModel: HomeFlowViewModel
    ) {
        homeFlowViewModel.moviePosters.value = moviePosters
    }

    override fun showPostersTvSeries(
        tvSeriesPosters: List<Poster>,
        homeFlowViewModel: HomeFlowViewModel
    ) {
        homeFlowViewModel.tvSeriesPosters.value = tvSeriesPosters
    }

    override fun showException(exception: String, mainActivityViewModel: MainActivityViewModel) {
        mainActivityViewModel.setExceptionToStateFlow(exception)
    }

    override fun showBootAutoStartAlertDialog(ma: MainActivity) =
        ma.mainActivityViewModel.setBootAutoStartAlertDialogToStateFlow(1)
}

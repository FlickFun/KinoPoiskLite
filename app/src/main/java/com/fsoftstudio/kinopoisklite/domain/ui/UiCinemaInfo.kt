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
package com.fsoftstudio.kinopoisklite.domain.ui

import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.ui.screens.cinema.CinemaViewModel
import io.reactivex.rxjava3.core.Completable

interface UiCinemaInfo {
    fun showCinemaInfo(cinemaInfo: CinemaInfo, cinemaViewModel: CinemaViewModel)
    fun showException(exception: String, cinemaViewModel: CinemaViewModel)
}
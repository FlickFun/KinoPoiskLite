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
package com.fsoftstudio.kinopoisklite.di.modules

import com.fsoftstudio.kinopoisklite.domain.ui.*
import com.fsoftstudio.kinopoisklite.ui.imp.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModule {

    @Binds
    abstract fun bindUiPosters(
        uiPostersImp: UiPostersImp
    ): UiPosters

    @Binds
    abstract fun bindUiListCinemaSearch(
        uiListCinemaSearchImp: UiListCinemaSearchImp
    ): UiListCinemaSearch

    @Binds
    abstract fun bindUiCinemaInfo(
        uiCinemaInfoImp: UiCinemaInfoImp
    ): UiCinemaInfo

    @Binds
    abstract fun bindUiListCinemaFavorite(
        uiListCinemaFavoriteImp: UiListCinemaFavoriteImp
    ): UiListCinemaFavorite

    @Binds
    abstract fun bindUiUserProfile(
        uiUserProfile: UiUserProfileImp
    ): UiUserProfile
}
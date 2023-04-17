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
package com.fsoftstudio.kinopoisklite.domain.usecase.inner

import android.annotation.SuppressLint
import com.fsoftstudio.kinopoisklite.data.FavoritesDataRepository
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.UserProfileUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteCinemaImp @Inject constructor(
    private val favoritesDataRepository: FavoritesDataRepository,
    private val userProfileUseCase: UserProfileUseCase
) : FavoriteCinema {

     @SuppressLint("CheckResult")
     override fun addFavoritesCinemaToFavoritesList(id: Int) {
         ListCinemaFavoriteUseCase.favorite?.add(id)
         favoritesDataRepository.addEntityIdToFavoritesEntities(id, userProfileUseCase.getLogin())
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({
             }, {
             })
     }

     @SuppressLint("CheckResult")
     override fun deleteFavoritesCinemaFromFavoritesList(id: Int) {
         ListCinemaFavoriteUseCase.favorite?.remove(id)
         favoritesDataRepository.deleteEntityIdFromFavoritesEntities(id, userProfileUseCase.getLogin())
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe({
             }, {
             })
     }

}
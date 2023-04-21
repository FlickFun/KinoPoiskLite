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
package com.fsoftstudio.kinopoisklite.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.databinding.CardPosterBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaSearchUseCase
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.presentation.animateWithStartDelay
import com.fsoftstudio.kinopoisklite.utils.changeFavorite
import com.fsoftstudio.kinopoisklite.utils.setCheckedFavorite
import com.fsoftstudio.kinopoisklite.utils.setPopularPosterImage
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class PostersGridAdapter constructor(
    private val posters: List<Poster>,
    private val listCinemaSearchUseCase: ListCinemaSearchUseCase
) : BaseAdapter() {

    override fun getCount(): Int {
        return posters.size
    }

    override fun getItem(position: Int): Any {
        return posters[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val posterBinding: CardPosterBinding = if (convertView == null) {
            CardPosterBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        } else {
            CardPosterBinding.bind(convertView)
        }.apply {
            val poster = posters[position]
            ivPoster.setPopularPosterImage(this, poster)
            CoroutineScope(Main).launch {

                ivSwitchFavoritePoster.setCheckedFavorite(
                    FavoriteIdsStorage.get().contains(poster.id)
                )
                ivSwitchFavoritePoster.animateWithStartDelay(500)

                tvPoster.text = poster.title
                tvPoster.animateWithStartDelay(500)
            }

            ivSwitchFavoritePoster.setOnClickListener {
                ivSwitchFavoritePoster.changeFavorite(poster, listCinemaSearchUseCase)
            }

            clCardPoster.setOnClickListener {
                MainActivity.mainActivity?.openCinemaInfo(poster)
            }
        }
        return posterBinding.root
    }
}

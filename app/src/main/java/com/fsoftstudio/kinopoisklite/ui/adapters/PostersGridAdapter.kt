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
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.databinding.CardPosterBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.utils.ImagesDownloader
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.File


class PostersGridAdapter(private val posters: List<Poster>) : BaseAdapter() {

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

        val poster: CardPosterBinding = if (convertView == null) {
            CardPosterBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        } else {
            CardPosterBinding.bind(convertView)
        }.apply {
            ivPoster.setPopularPosterImage(this, position)
            tvPoster.text = posters[position].title
            llCardPoster.setOnClickListener {
                posters[position].apply {
                    favorite = ListCinemaFavoriteUseCase.favorite?.contains(id) == true
                }
                MainActivity.mainActivity?.openCinemaInfo(posters[position])
            }
        }

        return poster.root
    }

    private fun ImageView.setPopularPosterImage(poster: CardPosterBinding, position: Int) =
        with(poster) {
            val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            }
            CoroutineScope(IO).launch(coroutineExceptionHandler) {
                fun showPoster() {
                    ivPoster.visibility = VISIBLE
                    pbPoster.visibility = GONE
                }

                when (val posterPath = posters[position].posterPath) {
                    null -> {
                        withContext(Main) {
                            setImageResource(R.drawable.round_no_photography_24)
                            showPoster()
                        }
                    }
                    else -> {
                        ImagesDownloader().getImage(
                            posterPath,
                            posters[position].id.toString(),
                            ivPoster,
                            pbPoster
                        ) {
                            launch(Main) {
                                setImageURI(Uri.fromFile(File(it)))
                                showPoster()
                            }
                        }
                    }
                }
            }
        }
}

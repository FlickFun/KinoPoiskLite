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
package com.fsoftstudio.kinopoisklite.ui.screens.cinema

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.databinding.ActivityCinemaInfoBinding
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.CinemaInfoUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import com.fsoftstudio.kinopoisklite.parameters.Sys.CINEMA
import com.fsoftstudio.kinopoisklite.parameters.Sys.ID_INT
import com.fsoftstudio.kinopoisklite.parameters.Sys.JPG
import com.fsoftstudio.kinopoisklite.parameters.Sys.LOCAL_POSTERS_FILES_PATH
import com.fsoftstudio.kinopoisklite.parameters.Sys.MIN
import com.fsoftstudio.kinopoisklite.parameters.Sys.NOTHING
import com.fsoftstudio.kinopoisklite.parameters.Sys.NO_DATA
import com.fsoftstudio.kinopoisklite.parameters.Sys.STAR_BOOLEAN
import com.fsoftstudio.kinopoisklite.parameters.Sys.TITLE
import com.fsoftstudio.kinopoisklite.utils.ShowInfo
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CinemaInfoActivity : AppCompatActivity() {

    private val cinemaViewModel: CinemaViewModel by viewModels()

    private lateinit var binding: ActivityCinemaInfoBinding

    private val arguments: Bundle by lazy { intent.extras!! }
    private val id: Int by lazy { arguments.getInt(ID_INT) }
    private val title: String? by lazy { arguments.getString(TITLE) }
    private val star: Boolean by lazy { arguments.getBoolean(STAR_BOOLEAN) }
    private val cinema: String by lazy { arguments.getString(CINEMA) ?: NOTHING }

    @Inject
    lateinit var showInfo: ShowInfo

    @Inject
    lateinit var cinemaInfoUseCase: CinemaInfoUseCase

    @Inject
    lateinit var appUseCase: AppUseCase

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        appUseCase.setTheme{}
        appUseCase.checkStatusBarColorDark(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCinemaInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()

        observeViewModel()

        setOnClickListener()

        cinemaViewModel.sendCinemaIdToDomain(id, cinema)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setData() = with(binding) {
        val filePath = LOCAL_POSTERS_FILES_PATH + id + JPG

        if (File(filePath).isFile) {
            ivInfo.setImageURI(Uri.fromFile(File(filePath)))
        } else {
            ivInfo.setImageDrawable(getDrawable(R.drawable.round_no_photography_24))
        }

        tvTitleInfo.text = title

        ibSwitchFavoriteCinema.setImageDrawable(binding.root.context.let {
            if (star) it.getDrawable(R.drawable.round_star_24)
            else it.getDrawable(R.drawable.round_star_border_24)
        })

        ibSwitchFavoriteCinema.setOnClickListener {
            if (ListCinemaFavoriteUseCase.favorite?.contains(id) == true) {
                cinemaInfoUseCase.deleteFavoriteCinemaFromFavoritesList(id)
                ibSwitchFavoriteCinema.setImageDrawable(binding.root.context.getDrawable(R.drawable.round_star_border_24))

            } else {
                cinemaInfoUseCase.addFavoriteCinemaToFavoritesList(id)
                ibSwitchFavoriteCinema.setImageDrawable(binding.root.context.getDrawable(R.drawable.round_star_24))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        cinemaViewModel.cinemaInfo.observe(this) { cinemaInfo ->
            binding.apply {
                tvTitleInfo.text = cinemaInfo.title
                tvReleaseDateInfo.text = cinemaInfo.releaseDate
                tvRuntimeInfo.text =
                    cinemaInfo.runtime + if (cinemaInfo.runtime != NO_DATA) MIN else ""
                tvActorsInfo.text = cinemaInfo.actors
                tvGenresInfo.text = cinemaInfo.genres
                tvOverviewInfo.text =
                    if (cinemaInfo.oveview?.isNotEmpty() == true) cinemaInfo.oveview else NO_DATA
                pbInfo.visibility = View.GONE
            }
        }
        cinemaViewModel.exception.observe(this) { exception ->
            if (exception.isNotEmpty()) {
                showInfo.snackbar(exception, this, binding.clForSnackbarCinemaInfo)
            }
        }
    }

    private fun setOnClickListener() = with(binding) {
        ibBackInfo.setOnClickListener {
            backPage()
        }
    }

    private fun backPage() {
        finish()
    }
}
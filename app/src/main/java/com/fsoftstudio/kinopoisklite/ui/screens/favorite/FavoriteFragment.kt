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

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.databinding.FragmentFavoriteBinding
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.UserProfileUseCase
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.FRAGMENT_FAVORITE
import com.fsoftstudio.kinopoisklite.ui.adapters.ListCinemaRcAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var listMovieRcAdapter: ListCinemaRcAdapter

    @Inject
    lateinit var listTvSeriesRcAdapter: ListCinemaRcAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        initCinemaRecycleView()
        observeViewModel()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.sendToDomainThatReadyToShowFavorite()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() = with(binding) {

        tvFavorite.text =
            (UserProfileUseCase.user?.login ?: getString(R.string.guest)).getFavoriteTitle()

        favoriteViewModel.moviePosters.observe(viewLifecycleOwner) { favoriteMovies ->
            if (favoriteMovies.isNotEmpty()) {
                rvMovieFavorite.visibility = VISIBLE
                tvNoFavoriteMovie.visibility = GONE

                val favoriteSet = getFavoriteHahSet()
                if (!favoriteSet.isNullOrEmpty()) {
                    listMovieRcAdapter.updateRcAdapter(
                        newPosters = favoriteMovies.filter { e -> favoriteSet.contains(e.id) },
                        fromFragment = FRAGMENT_FAVORITE
                    )
                }
            } else {
                rvMovieFavorite.visibility = GONE
                tvNoFavoriteMovie.visibility = VISIBLE
            }
        }

        favoriteViewModel.tvSeriesPosters.observe(viewLifecycleOwner) { favoriteTvSeries ->
            if (favoriteTvSeries.isNotEmpty()) {
                rvTvSeriesFavorite.visibility = VISIBLE
                tvNoFavoriteTvSeries.visibility = GONE

                val favoriteSet = getFavoriteHahSet()
                if (!favoriteSet.isNullOrEmpty()) {
                    listTvSeriesRcAdapter.updateRcAdapter(
                        newPosters = favoriteTvSeries.filter { e -> favoriteSet.contains(e.id) },
                        fromFragment = FRAGMENT_FAVORITE
                    )
                }
            } else {
                rvTvSeriesFavorite.visibility = GONE
                tvNoFavoriteTvSeries.visibility = VISIBLE
            }
        }
    }

    private fun getFavoriteHahSet() = ListCinemaFavoriteUseCase.favorite

    private fun String.getFavoriteTitle(): SpannableStringBuilder {
        val favorite = "${getString(R.string.favorite)} - "
        return SpannableStringBuilder(favorite + this).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                favorite.length - 1,
                this.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
    }

    private fun initCinemaRecycleView() = with(binding) {

        rvMovieFavorite.layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
        rvMovieFavorite.adapter = listMovieRcAdapter

        rvTvSeriesFavorite.layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
        rvTvSeriesFavorite.adapter = listTvSeriesRcAdapter
    }
}
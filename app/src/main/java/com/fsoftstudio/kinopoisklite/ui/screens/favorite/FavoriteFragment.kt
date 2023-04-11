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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.databinding.FragmentFavoriteBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.UserProfileUseCase
import com.fsoftstudio.kinopoisklite.ui.adapters.ListCinemaRcAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var searchMovies: List<Poster>? = null
    private var searchTvSeries: List<Poster>? = null

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
        showFavorite()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() = with(binding) {

        favoriteViewModel.moviePosters.observe(viewLifecycleOwner) { searchMovies ->
            tvFavorite.text = "${getString(R.string.favorite)} - ${UserProfileUseCase.user?.login ?: getString(R.string.guest)}"
            if (searchMovies.isNotEmpty()) {
                rvMovieFavorite.visibility = View.VISIBLE
                tvNoFavoriteMovie.visibility = View.GONE
                listMovieRcAdapter.updateSearchRcAdapter(searchMovies)
            } else {
                rvMovieFavorite.visibility = View.GONE
                tvNoFavoriteMovie.visibility = View.VISIBLE
            }
            this@FavoriteFragment.searchMovies = searchMovies
        }

        favoriteViewModel.tvSeriesPosters.observe(viewLifecycleOwner) { searchTvSeries ->
            if (searchTvSeries.isNotEmpty()) {
                rvTvSeriesFavorite.visibility = View.VISIBLE
                tvNoFavoriteTvSeries.visibility = View.GONE
                listTvSeriesRcAdapter.updateSearchRcAdapter(searchTvSeries)
            } else {
                rvTvSeriesFavorite.visibility = View.GONE
                tvNoFavoriteTvSeries.visibility = View.VISIBLE
            }
            this@FavoriteFragment.searchTvSeries = searchTvSeries
        }
    }

    private fun initCinemaRecycleView() = with(binding) {

        rvMovieFavorite.layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
        rvMovieFavorite.adapter = listMovieRcAdapter

        rvTvSeriesFavorite.layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
        rvTvSeriesFavorite.adapter = listTvSeriesRcAdapter
    }

    private fun showFavorite() {
        favoriteViewModel.sendToDomainThatReadyToShowFavorite()
        searchMovies?.let { listMovieRcAdapter.updateSearchRcAdapter(it) }
        searchTvSeries?.let { listTvSeriesRcAdapter.updateSearchRcAdapter(it) }
    }

}
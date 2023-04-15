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
package com.fsoftstudio.kinopoisklite.ui.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoftstudio.kinopoisklite.ui.adapters.ListCinemaRcAdapter
import com.fsoftstudio.kinopoisklite.databinding.FragmentSearchBinding
import com.fsoftstudio.kinopoisklite.parameters.Sys.MOVIE
import com.fsoftstudio.kinopoisklite.parameters.Sys.TV_SERIES
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setOnClickListener()
        initCinemaRecycleView()
        observeViewModel()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showLastResult()
    }

    override fun onResume() {
        super.onResume()
        checkFavoriteChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkFavoriteChanges() {
        ListCinemaRcAdapter.positionItem?.let {
            when (ListCinemaRcAdapter.cinema) {
                MOVIE -> {
                    listMovieRcAdapter.checkNotifyItemChanged(it)
                }
                TV_SERIES -> {
                    listTvSeriesRcAdapter.checkNotifyItemChanged(it)
                }
            }
            ListCinemaRcAdapter.positionItem = null
            ListCinemaRcAdapter.cinema = null
        }
    }
    private fun setOnClickListener() = with(binding) {
        bSearch.setOnClickListener {
            searchViewModel.sendTextSearchToDomain(tietSearch.text.toString())
            showViews()
        }
    }

    private fun showViews(isFirstOpen: Boolean = true) {
        binding.apply {
            svSearch.visibility = VISIBLE
            rvMovieSearch.visibility = VISIBLE
            rvTvSeriesSearch.visibility = VISIBLE
            tvNothingFindMoviesSearch.visibility = GONE
            tvNothingFindTvSeriesSearch.visibility = GONE
            if (isFirstOpen) {
                pbRvMovieSearch.visibility = VISIBLE
                pbRvTvSeriesSearch.visibility = VISIBLE
            }
        }
    }

    private fun observeViewModel() = with(binding) {
        searchViewModel.moviePosters.observe(viewLifecycleOwner) { searchMovies ->
            if (searchMovies.isNotEmpty()) {
                tvNothingFindMoviesSearch.visibility = GONE
                listMovieRcAdapter.updateRcAdapter(newPosters = searchMovies)
            } else {
                rvMovieSearch.visibility = GONE
                pbRvMovieSearch.visibility = GONE
                tvNothingFindMoviesSearch.visibility = VISIBLE
                listMovieRcAdapter.updateRcAdapter(newPosters = searchMovies)
            }
            pbRvMovieSearch.visibility = GONE
        }

        searchViewModel.tvSeriesPosters.observe(viewLifecycleOwner) { searchTvSeries ->
            if (searchTvSeries.isNotEmpty()) {
                tvNothingFindTvSeriesSearch.visibility = GONE
                listTvSeriesRcAdapter.updateRcAdapter(newPosters = searchTvSeries)
            } else {
                rvTvSeriesSearch.visibility = GONE
                pbRvTvSeriesSearch.visibility = GONE
                tvNothingFindTvSeriesSearch.visibility = VISIBLE
                listTvSeriesRcAdapter.updateRcAdapter(newPosters = searchTvSeries)
            }
            pbRvTvSeriesSearch.visibility = GONE
        }
    }

    private fun initCinemaRecycleView() = with(binding) {
        rvMovieSearch.layoutManager = LinearLayoutManager(this@SearchFragment.context)
        rvMovieSearch.adapter = listMovieRcAdapter

        rvTvSeriesSearch.layoutManager = LinearLayoutManager(this@SearchFragment.context)
        rvTvSeriesSearch.adapter = listTvSeriesRcAdapter
    }

    private fun showLastResult() {
        if (binding.tietSearch.text.toString().isNotEmpty()) {
            showViews(false)
        }
    }
}
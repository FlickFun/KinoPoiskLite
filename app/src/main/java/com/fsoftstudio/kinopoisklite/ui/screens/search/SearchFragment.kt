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
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fsoftstudio.kinopoisklite.ui.adapters.ListCinemaRcAdapter
import com.fsoftstudio.kinopoisklite.databinding.FragmentSearchBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel : SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setOnClickListener()
        initCinemaRecycleView()
        observeViewModel()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showFoundResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setOnClickListener() {
        binding.apply {
            bSearch.setOnClickListener {
                searchViewModel.sendTextSearchToDomain(tietSearch.text.toString())
                showViews()
            }
        }
    }

    private fun showViews() {
        binding.apply {
            svSearch.visibility = View.VISIBLE
            rvMovieSearch.visibility = View.VISIBLE
            rvTvSeriesSearch.visibility = View.VISIBLE
            pbRvMovieSearch.visibility = View.VISIBLE
            pbRvTvSeriesSearch.visibility = View.VISIBLE
            tvNothingFindMoviesSearch.visibility = View.GONE
            tvNothingFindTvSeriesSearch.visibility = View.GONE
        }
    }

    private fun observeViewModel() = with(binding) {
        searchViewModel.moviePosters.observe(viewLifecycleOwner) { searchMovies ->
            if (searchMovies.isNotEmpty()) {
                tvNothingFindMoviesSearch.visibility = View.GONE
                listMovieRcAdapter.updateSearchRcAdapter(searchMovies)
            } else {
                rvMovieSearch.visibility = View.GONE
                pbRvMovieSearch.visibility = View.GONE
                tvNothingFindMoviesSearch.visibility = View.VISIBLE
                listMovieRcAdapter.updateSearchRcAdapter(searchMovies)
            }
            this@SearchFragment.searchMovies = searchMovies
            pbRvMovieSearch.visibility = View.GONE
        }

        searchViewModel.tvSeriesPosters.observe(viewLifecycleOwner) { searchTvSeries ->
            if (searchTvSeries.isNotEmpty()) {
                tvNothingFindTvSeriesSearch.visibility = View.GONE
                listTvSeriesRcAdapter.updateSearchRcAdapter(searchTvSeries)
            } else {
                rvTvSeriesSearch.visibility = View.GONE
                pbRvTvSeriesSearch.visibility = View.GONE
                tvNothingFindTvSeriesSearch.visibility = View.VISIBLE
                listTvSeriesRcAdapter.updateSearchRcAdapter(searchTvSeries)
            }
            this@SearchFragment.searchTvSeries = searchTvSeries
            pbRvTvSeriesSearch.visibility = View.GONE
        }
    }

    private fun initCinemaRecycleView() = with(binding) {
        rvMovieSearch.layoutManager = LinearLayoutManager(this@SearchFragment.context)
        rvMovieSearch.adapter = listMovieRcAdapter

        rvTvSeriesSearch.layoutManager = LinearLayoutManager(this@SearchFragment.context)
        rvTvSeriesSearch.adapter = listTvSeriesRcAdapter
    }

    private fun showFoundResult() {
        binding.tietSearch.text.toString().also { text->
            if (text.isNotEmpty()) {
                searchViewModel.sendTextSearchToDomain(text)
                searchMovies?.let { listMovieRcAdapter.updateSearchRcAdapter(it) }
                searchTvSeries?.let { listTvSeriesRcAdapter.updateSearchRcAdapter(it) }
                showViews()
            }
        }
    }
}
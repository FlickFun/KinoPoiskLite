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
package com.fsoftstudio.kinopoisklite.ui.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fsoftstudio.kinopoisklite.ui.adapters.PostersGridAdapter
import com.fsoftstudio.kinopoisklite.databinding.FragmentHomeBinding
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeFlowViewModel: HomeFlowViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var appUseCase: AppUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (MainActivity.isFragmentReadyToChangeTheme) {
            MainActivity.isFragmentReadyToChangeTheme = false
            collectViewModels()
            homeFlowViewModel.sendToDomainThatReadyShowPosters(binding)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun collectViewModels() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val gridViewMovies: GridView = binding.gvMovies
                setOnTouchListener(gridViewMovies)
                homeFlowViewModel.moviePosters.collect { moviePosters ->
                    moviePosters?.let {
                        if (it.isNotEmpty()) {
                            homeFlowViewModel.showLogo(false, binding)
                            gridViewMovies.adapter =
                                PostersGridAdapter(moviePosters)
                            binding.pbGvMovieHome.visibility = GONE
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val gridViewTvSeries: GridView = binding.gvTvSeries
                setOnTouchListener(gridViewTvSeries)
                homeFlowViewModel.tvSeriesPosters.collect { tvSeriesPosters ->
                    tvSeriesPosters?.let {
                        if (it.isNotEmpty()) {
                            homeFlowViewModel.showLogo(false, binding)
                            gridViewTvSeries.adapter =
                                PostersGridAdapter(tvSeriesPosters)
                            binding.pbGvTvSeriesHome.visibility = GONE
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(gridView: GridView) {
        gridView.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }
    }
}
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

import android.view.View
import androidx.lifecycle.ViewModel
import com.fsoftstudio.kinopoisklite.databinding.FragmentHomeBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.PostersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFlowViewModel @Inject constructor(
    private val postersUseCase: PostersUseCase
) : ViewModel() {

    private var _moviePosters: MutableStateFlow<List<Poster>?> = MutableStateFlow(null)
    var moviePosters: MutableStateFlow<List<Poster>?>
        get() = _moviePosters
        set(mp) {
            _moviePosters = mp
        }

    private var _tvSeriesPosters: MutableStateFlow<List<Poster>?> = MutableStateFlow(null)
    var tvSeriesPosters: MutableStateFlow<List<Poster>?>
        get() = _tvSeriesPosters
        set(sp) {
            _tvSeriesPosters = sp
        }

    private val compositeDisposable = CompositeDisposable()

    fun sendToDomainThatReadyShowPosters(binding: FragmentHomeBinding) {
        if (AppUseCase.isCanUpdate) {
            postersUseCase.readyToShowPosters(
                compositeDisposable = compositeDisposable,
                homeFlowViewModel = this
            )
        } else {
            showLogo(true, binding)
            postersUseCase.readyToShowPosters(
                isCanUpdate = false,
                compositeDisposable = compositeDisposable,
                homeFlowViewModel = this
            )
        }
    }

    fun showLogo(isShowLogo: Boolean, binding: FragmentHomeBinding) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.clLogoHome.visibility = if (isShowLogo) View.VISIBLE else View.GONE
            binding.svHome.visibility = if (isShowLogo) View.GONE else View.VISIBLE
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
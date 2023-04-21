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
package com.fsoftstudio.kinopoisklite.utils

import android.content.Context
import android.view.View
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.ExceptionsUseCase.Companion.EXCEPTION_POSTERS
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ShowInfo @Inject constructor(
    private val exceptionsUseCase: ExceptionsUseCase
) {

    fun snackbar(
        message: String,
        context: Context,
        view: View,
        snackbarLength: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            Snackbar.make(view, message, snackbarLength)
                .setAction(context.getString(R.string.ok)) {
                    if (view == MainActivity.mainActivity?.findViewById(R.id.cl_for_snackbar_main)) {
                        exceptionsUseCase.setEmptyException(EXCEPTION_POSTERS)
                    }
                }
                .setTextMaxLines(45)
                .show()
        }
    }

}

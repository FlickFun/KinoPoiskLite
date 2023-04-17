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
package com.fsoftstudio.kinopoisklite.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {

    private var _exception: MutableStateFlow<String?> = MutableStateFlow(null)
    var exception: MutableStateFlow<String?>
        get() = _exception
        set(ex) {
            _exception = ex
        }
    private var _bootAutoStartAlertDialog: MutableStateFlow<Int?> = MutableStateFlow(null)
    var bootAutoStartAlertDialog: MutableStateFlow<Int?>
        get() = _bootAutoStartAlertDialog
        set(ad) {
            _bootAutoStartAlertDialog = ad
        }

    fun setExceptionToStateFlow(exception: String) {
        _exception.value = exception
    }

    fun setBootAutoStartAlertDialogToStateFlow(key: Int?) {
        _bootAutoStartAlertDialog.value = key
    }
}
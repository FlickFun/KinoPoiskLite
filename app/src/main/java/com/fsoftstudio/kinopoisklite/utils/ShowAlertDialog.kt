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
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.BOOT_AUTO_START_ASC_LATE
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.BOOT_AUTO_START_DO_NOT_ASC
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.BOOT_AUTO_START_OPEN_SETTINGS
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShowAlertDialog {
    fun showBootAutoStart(ctx: Context) {
        MaterialAlertDialogBuilder(ctx)
            .setTitle(ctx.getString(R.string.notice))
            .setMessage(ctx.getString(R.string.boot_auto_start_description))
            .setNeutralButton(ctx.getString(R.string.don_t_asc)) { dialog, _ ->
                sendResponseBootAlertDialogAutoStart(ctx, BOOT_AUTO_START_DO_NOT_ASC)
                dialog.dismiss()
            }
            .setNegativeButton(ctx.getString(R.string.late)) { dialog, _ ->
                sendResponseBootAlertDialogAutoStart(ctx, BOOT_AUTO_START_ASC_LATE)
                dialog.dismiss()
            }
            .setPositiveButton(ctx.getString(R.string.go_to_settings)) { dialog, _ ->
                sendResponseBootAlertDialogAutoStart(ctx, BOOT_AUTO_START_OPEN_SETTINGS)
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
    private fun sendResponseBootAlertDialogAutoStart(ctx: Context, BOOT_AUTO_START_DO_NOT_ASC: Int) {
        (ctx as MainActivity).showAlertDialogAndSaveResult(BOOT_AUTO_START_DO_NOT_ASC)
    }
}
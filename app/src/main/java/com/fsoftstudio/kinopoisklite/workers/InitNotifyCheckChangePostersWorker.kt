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
package com.fsoftstudio.kinopoisklite.workers

import android.content.Context
import androidx.work.*
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.NOTIFICATION_WORK
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.TAG_OUTPUT
import java.util.concurrent.TimeUnit

class InitNotifyCheckChangePostersWorker(private val application: Context) {
    fun checkPosters() {
        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val checkPostersRequest =
            PeriodicWorkRequestBuilder<NotifyCheckChangePostersWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .addTag(TAG_OUTPUT)
                .build()

        val workManager: WorkManager = WorkManager.getInstance(application.applicationContext)
        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            checkPostersRequest
        )
    }
}
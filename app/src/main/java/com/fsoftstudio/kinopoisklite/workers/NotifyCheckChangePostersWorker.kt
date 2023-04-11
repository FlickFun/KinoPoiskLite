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

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.domain.usecase.AppUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.PostersUseCase
import com.fsoftstudio.kinopoisklite.parameters.Sys.NOTIFICATION_CHANNEL
import com.fsoftstudio.kinopoisklite.parameters.Sys.NOTIFICATION_ID
import com.fsoftstudio.kinopoisklite.parameters.Sys.NOTIFICATION_NAME
import com.fsoftstudio.kinopoisklite.parameters.Sys.TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

@HiltWorker
class NotifyCheckChangePostersWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted param: WorkerParameters,
    private val postersUseCase: PostersUseCase,
    private val appUseCase: AppUseCase
) : CoroutineWorker(ctx, param) {

    override suspend fun doWork(): Result {
        Log.i(
            TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER,
            "Enter NotifyCheckChangePostersWorker -> "
        )
        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
        return try {
            withContext(IO) {

                appUseCase.setApiKey(false)

                postersUseCase.checkChangePoster { isPostersChanged ->
                    if (isPostersChanged) {
                        sendNotification(id)
                    }
                    Log.i(
                        TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER,
                        "Take checkChangePoster callback = $isPostersChanged -> then maybe check and sendNotification($id) ->"
                    )
                }
            }
            Log.i(TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER, "Before Result.success() -> ")
            Result.success()
        } catch (e: Exception) {
            Log.i(
                TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER,
                "Error NotifyCheckChangePostersWorker -> $e"
            )
            Result.failure()
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        val bitmap = applicationContext.vectorToBitmap(R.drawable.round_local_movies_24)
        val bitmap =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.round_local_movies_24)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableLights(true)
            channel.lightColor = Color.MAGENTA
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id, notification.build())
    }
}
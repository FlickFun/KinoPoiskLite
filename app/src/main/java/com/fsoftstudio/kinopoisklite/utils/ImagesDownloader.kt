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

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.fsoftstudio.kinopoisklite.parameters.Sys.JPG
import com.fsoftstudio.kinopoisklite.parameters.Sys.LOCAL_POSTERS_FILES_PATH
import com.fsoftstudio.kinopoisklite.parameters.Sys.URI_PATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImagesDownloader @Inject constructor() {

    fun getImage(
        name: String,
        id: String, imageView: ImageView, pb: ProgressBar, callback: (String) -> Unit
    ) {
        val filePath = LOCAL_POSTERS_FILES_PATH + id + JPG
        val url = URL(URI_PATH + name)

        var isFile: Boolean
        if (File(filePath).isFile.also { isFile = it }
            && File(filePath).length() != 0L && File(filePath).canRead()) {
            callback.invoke(filePath)
        } else {
            showDownloadProcess(imageView, pb)
            if (isFile) {
                File(filePath).delete()
            }
            downloadFile(url, filePath) {
                callback.invoke(filePath)
            }
        }
    }

    private fun showDownloadProcess(imageView: ImageView, pb: ProgressBar) {
        CoroutineScope(Dispatchers.Main).launch {
            imageView.visibility = View.VISIBLE
            pb.visibility = View.VISIBLE
        }
    }

    private fun downloadFile(url: URL, filePath: String, callback: () -> Unit) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(filePath).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                    callback.invoke()
                }
            }
        }
    }
}
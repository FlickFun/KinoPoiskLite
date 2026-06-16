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
package com.fsoftstudio.kinopoisklite.data.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.fsoftstudio.kinopoisklite.common.entity.Const.JPG
import com.fsoftstudio.kinopoisklite.common.entity.Const.URI_PATH
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.dnsoverhttps.DnsOverHttps
import java.io.File
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImagesDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val postersDir: File by lazy {
        File(context.filesDir, "posters").apply { if (!exists()) mkdirs() }
    }

    private val client: OkHttpClient by lazy {
        val bootstrapClient = OkHttpClient.Builder().build()
        val dns = DnsOverHttps.Builder()
            .client(bootstrapClient)
            .url("https://1.1.1.1/dns-query".toHttpUrl())
            .bootstrapDnsHosts(listOf(
                InetAddress.getByAddress(byteArrayOf(1, 1, 1, 1)),
                InetAddress.getByAddress(byteArrayOf(1, 0, 0, 1))
            ))
            .build()

        OkHttpClient.Builder()
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    val addresses = try {
                        dns.lookup(hostname)
                    } catch (e: Exception) {
                        listOf(InetAddress.getByName("8.8.8.8"))
                    }
                    val filtered = addresses.filter { !it.isLoopbackAddress && it.hostAddress != "127.0.0.1" }
                    return filtered.ifEmpty { listOf(InetAddress.getByName("8.8.4.4")) }
                }
            })
            .build()
    }

    fun getImage(
        name: String?,
        id: String,
        imageView: ImageView,
        pb: ProgressBar,
        callback: (Bitmap) -> Unit
    ) {
        if (name == null) {
            pb.visibility = View.GONE
            return
        }

        val file = File(postersDir, id + JPG)
        val url = URI_PATH + name

        imageView.tag = id

        if (file.exists() && file.length() > 0) {
            CoroutineScope(IO).launch {
                val bitmap = decodeFile(file.absolutePath)
                withContext(Main) {
                    if (imageView.tag == id && bitmap != null) {
                        pb.visibility = View.GONE
                        callback.invoke(bitmap)
                    } else if (bitmap == null) {
                        // Если файл битый, пробуем перекачать
                        file.delete()
                        downloadAndShow(url, file, id, imageView, pb, callback)
                    }
                }
            }
        } else {
            downloadAndShow(url, file, id, imageView, pb, callback)
        }
    }

    private fun downloadAndShow(
        url: String,
        file: File,
        id: String,
        imageView: ImageView,
        pb: ProgressBar,
        callback: (Bitmap) -> Unit
    ) {
        pb.visibility = View.VISIBLE
        CoroutineScope(IO).launch {
            try {
                if (downloadFile(url, file)) {
                    val bitmap = decodeFile(file.absolutePath)
                    withContext(Main) {
                        if (imageView.tag == id && bitmap != null) {
                            pb.visibility = View.GONE
                            callback.invoke(bitmap)
                        } else {
                            pb.visibility = View.GONE
                        }
                    }
                } else {
                    withContext(Main) {
                        pb.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Main) {
                    pb.visibility = View.GONE
                }
            }
        }
    }

    private fun decodeFile(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    private fun downloadFile(url: String, file: File): Boolean {
        val request = Request.Builder().url(url).build()
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return false
                response.body?.let { body ->
                    file.parentFile?.mkdirs()
                    file.outputStream().use { output ->
                        body.byteStream().use { input ->
                            input.copyTo(output)
                        }
                    }
                    return true
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }
}

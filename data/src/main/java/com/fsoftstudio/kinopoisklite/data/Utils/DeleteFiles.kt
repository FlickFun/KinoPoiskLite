package com.fsoftstudio.kinopoisklite.data.Utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.fsoftstudio.kinopoisklite.common.CommonUi
import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.common.Logger
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_CANT_CLEAN_CACHE
import com.fsoftstudio.kinopoisklite.common.entity.Const.NOTICE_CACHE_CLEARED
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.io.path.nameWithoutExtension

class DeleteFiles @Inject constructor(
    @ApplicationContext private val context: Context,
    private val commonUi: CommonUi,
    private val logger: Logger
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteAllFilesFromPosterDirectory() {
        val postersDir = File(context.filesDir, "posters")
        deleteAllFilesFromDirectory(postersDir.absolutePath)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteAllFilesFromDirectory(filesDirectory: String) {

        var count = 1
        if (Files.isDirectory(Paths.get(filesDirectory))) {
            try {
                for (path in Files.list(
                    Paths.get(filesDirectory)
                ).collect(Collectors.toList())) {

                    if (!FavoriteIdsStorage.get().contains(path.nameWithoutExtension.toInt())) {
                        count++
                        Files.deleteIfExists(path)
                    }
                }
                commonUi.toast(NOTICE_CACHE_CLEARED)

            } catch (e: IOException) {
                logger.err(e)
                commonUi.toast(ERROR_CANT_CLEAN_CACHE)
            }
        }
        logger.log("[${javaClass.simpleName}] [${--count}] FILES - SUCCESSFULLY DELETED!")
    }
}
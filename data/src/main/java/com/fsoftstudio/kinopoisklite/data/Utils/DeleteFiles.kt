package com.fsoftstudio.kinopoisklite.data.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.fsoftstudio.kinopoisklite.common.CommonUi
import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.common.Logger
import com.fsoftstudio.kinopoisklite.common.entity.Const.ERROR_CANT_CLEAN_CACHE
import com.fsoftstudio.kinopoisklite.common.entity.Const.LOCAL_POSTERS_FILES_PATH
import com.fsoftstudio.kinopoisklite.common.entity.Const.NOTICE_CACHE_CLEARED
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject
import kotlin.io.path.nameWithoutExtension

class DeleteFiles @Inject constructor(
    private val commonUi: CommonUi,
    private val logger: Logger
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteAllFilesFromPosterDirectory() =
        deleteAllFilesFromDirectory(LOCAL_POSTERS_FILES_PATH)

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
package ccom.fsoftstudio.kinopoisklite.common_impl

import android.util.Log
import com.fsoftstudio.kinopoisklite.common.Logger
import com.fsoftstudio.kinopoisklite.common.entity.Const.TAG_MOVIE_BASE
import javax.inject.Inject

class AndroidLogger @Inject constructor(): Logger {

    override fun log(message: String) {
        Log.d(TAG_MOVIE_BASE, message)
    }

    override fun err(exception: Throwable, message: String?) {
        Log.e(TAG_MOVIE_BASE, message, exception)
    }

}
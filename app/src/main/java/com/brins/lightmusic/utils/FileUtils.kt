package com.brins.lightmusic.utils

import android.content.Context
import java.io.File

object FileUtils {

    @JvmStatic
    fun getAudioCacheDir(context: Context): File {
        return File(context.getExternalFilesDir("music"), "audio-cache")
    }
}
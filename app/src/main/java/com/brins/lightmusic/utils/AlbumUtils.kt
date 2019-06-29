package com.brins.lightmusic.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import org.spongycastle.util.encoders.Hex
import java.io.ByteArrayOutputStream
import java.lang.Exception

class AlbumUtils {

    companion object {
        @JvmStatic
        fun loadingCover(mediaUri: String): String {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(mediaUri)
            var picture = mediaMetadataRetriever.embeddedPicture
            if (picture == null)
            {
                val btStream = ByteArrayOutputStream()
                val bitmap = BitmapFactory.decodeResource(
                    BaseApplication.getInstance().applicationContext.resources,
                    R.drawable.default_cover)
                bitmap.compress(Bitmap.CompressFormat.PNG , 100 ,btStream)
                picture = btStream.toByteArray()
            }
            return Hex.toHexString(picture)
        }

        @JvmStatic
        fun String2Bitmap(bitmapString: String): Bitmap? {
            var bitmap: Bitmap? = null
            val b = Hex.decode(bitmapString)
            try {
                bitmap = BitmapFactory.decodeByteArray(
                    b, 0,
                    b.size
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap ?: BitmapFactory.decodeResource(
                BaseApplication.getInstance().applicationContext.resources,
                R.drawable.default_cover
            )
        }
    }
}
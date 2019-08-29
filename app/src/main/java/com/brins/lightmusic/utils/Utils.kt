package com.brins.lightmusic.utils

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.collection.SimpleArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import org.spongycastle.util.encoders.Hex
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.lang.ref.WeakReference

val option = RequestOptions()
    .error(R.drawable.default_cover)
    .diskCacheStrategy(DiskCacheStrategy.ALL)

fun loadingCover(mediaUri: String): String {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(mediaUri)
    val picture = mediaMetadataRetriever.embeddedPicture
    return if (picture == null) getStringCover() else Hex.toHexString(picture)
}

fun loadingOnlineCover(url : String): Bitmap{
    return Glide.with(LightMusicApplication.getLightApplication())
        .asBitmap()
        .load(url)
        .apply(option)
        .submit(400,400)
        .get()
}

fun getStringCover(bitmap: Bitmap? = null): String {
        val btStream = ByteArrayOutputStream()
        val bitmapTemp = bitmap ?: BitmapFactory.decodeResource(
            BaseApplication.getInstance().applicationContext.resources,
            R.drawable.default_cover)
    bitmapTemp.compress(Bitmap.CompressFormat.PNG , 100 ,btStream)
        val resultpicture = btStream.toByteArray()
        return Hex.toHexString(resultpicture)
}


fun string2Bitmap(bitmapString: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val b = Hex.decode(bitmapString)
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

fun getAudioCacheDir(context: Context): File {
    return File(context.getExternalFilesDir("music"), "audio-cache")
}

fun create(@ColorInt startColor : Int, @ColorInt endColor : Int,
           radius : Int, @FloatRange(from = 0.0, to = 1.0) centerX : Float,
           @FloatRange(from = 0.0, to = 1.0) centerY : Float): GradientDrawable {

    val gradientDrawable = GradientDrawable()
    gradientDrawable.colors = intArrayOf(startColor, endColor)
    gradientDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
    gradientDrawable.gradientRadius = radius.toFloat()
    gradientDrawable.setGradientCenter(centerX, centerY)
    return gradientDrawable
}

/**
 * 获取当前进程名
 * @param context
 * @return
 */
fun getCurrProcessName(context: Context): String? {
    try {
        val currProcessId = android.os.Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        if (processInfos != null) {
            for (info in processInfos) {
                if (info.pid == currProcessId) {
                    return info.processName
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}


fun formatDuration(duration: Int): String{
    val durationSecond = duration / 1000
    var minute = durationSecond / 60
    val hour = minute / 60
    minute %= 60
    val second = durationSecond % 60
    return if (hour != 0)
        String.format("%2d:%02d:%02d", hour, minute, second)
    else
        String.format("%02d:%02d", minute, second)


}

val TYPEFACE_CACHE = SimpleArrayMap<String, WeakReference<Typeface>>()
private fun getTypefacePath(fontType: Int): String? {
    var typefacePath: String? = null
    when (fontType) {
        1 -> typefacePath = "fonts/DIN-Bold.otf"
        2 -> typefacePath = "fonts/DIN-Medium.otf"
        3 -> typefacePath = "fonts/DIN-Regular.otf"
        4 -> typefacePath = "fonts/LilyScriptOne-Regular.ttf"
        else ->{}
    }
    return typefacePath
}

fun getTypeface(context: Context, fontType: Int): Typeface? {
    val typefacePath = getTypefacePath(fontType)
    var typeface: Typeface? = null
    if (TYPEFACE_CACHE.containsKey(typefacePath)) {
        val typefaceWr = TYPEFACE_CACHE.get(typefacePath)
        if (typefaceWr != null) {
            typeface = typefaceWr.get()
        }
    }
    if (typeface == null) {
        typeface = Typeface.createFromAsset(context.assets, typefacePath)
        TYPEFACE_CACHE.put(typefacePath, WeakReference(typeface))
    }
    return typeface
}

fun show(str : String){
    if (str.isNotEmpty()){
        AndroidSchedulers.mainThread().scheduleDirect { Toast.makeText(LightMusicApplication.getLightApplication(),str,Toast.LENGTH_SHORT).show() }
    }
}

fun show(strId: Int) {
    if (strId != 0) {
        // 预防从非主线程中调用崩溃，这里直接切换到主线程中执行
        AndroidSchedulers.mainThread()
            .scheduleDirect { Toast.makeText(LightMusicApplication.getLightApplication(), strId, Toast.LENGTH_SHORT).show() }
    }
}


class SpacesItemDecoration(var space: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = 4 * space
    }
}


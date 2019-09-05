package com.brins.lightmusic.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
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
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.lang.Exception
import java.lang.ref.WeakReference

val option = RequestOptions()
    .error(R.drawable.default_cover)
    .diskCacheStrategy(DiskCacheStrategy.ALL)

fun loadingCover(mediaUri: String): String {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(mediaUri)
    val picture = mediaMetadataRetriever.embeddedPicture
    return if (picture == null) getStringCover() else Base64.encodeToString(picture, Base64.DEFAULT)
}

fun loadingOnlineCover(url: String): Bitmap {
    return Glide.with(LightMusicApplication.getLightApplication())
        .asBitmap()
        .load(url)
        .apply(option)
        .submit(400, 400)
        .get()
}

fun getStringCover(bitmap: Bitmap? = null): String {
    val btStream = ByteArrayOutputStream()
    val bitmapTemp = bitmap ?: BitmapFactory.decodeResource(
        BaseApplication.getInstance().applicationContext.resources,
        R.drawable.default_cover
    )
    bitmapTemp.compress(Bitmap.CompressFormat.PNG, 100, btStream)
    val resultpicture = btStream.toByteArray()
    return Base64.encodeToString(resultpicture, Base64.DEFAULT)
}


fun string2Bitmap(bitmapString: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val b = Base64.decode(bitmapString, Base64.DEFAULT)
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

fun create(
    @ColorInt startColor: Int, @ColorInt endColor: Int,
    radius: Int, @FloatRange(from = 0.0, to = 1.0) centerX: Float,
    @FloatRange(from = 0.0, to = 1.0) centerY: Float
): GradientDrawable {

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


fun formatDuration(duration: Int): String {
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
        else -> {
        }
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

fun show(str: String) {
    if (str.isNotEmpty()) {
        AndroidSchedulers.mainThread().scheduleDirect {
            Toast.makeText(LightMusicApplication.getLightApplication(), str, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

fun show(strId: Int) {
    if (strId != 0) {
        // 预防从非主线程中调用崩溃，这里直接切换到主线程中执行
        AndroidSchedulers.mainThread()
            .scheduleDirect {
                Toast.makeText(
                    LightMusicApplication.getLightApplication(),
                    strId,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}


class SpacesItemDecoration(var space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = 4 * space
    }
}

fun inputAnimator(view: View, w: Float, h: Float): AnimatorSet {

    val set = AnimatorSet()
    val animator = ValueAnimator.ofFloat(0f, w)
    animator.addUpdateListener {
        val value: Float = it.animatedValue as Float
        var params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = value.toInt()
        params.rightMargin = value.toInt()
        view.layoutParams = params
    }

    val animator2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f)
    set.duration = 1000
    set.interpolator = AccelerateDecelerateInterpolator()
    set.playTogether(animator, animator2)
    return set
}

fun progressAnimator(view: View) {

    val animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f)
    val animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f)
    val animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2)
    animator3.duration = 1000
    animator3.interpolator = JellyInterpolator()
    animator3.start()
}

fun recovery(view: View) {
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.leftMargin = 0
    params.rightMargin = 0
    view.layoutParams = params
    val animator = ObjectAnimator.ofFloat(
        view, "scaleX"
        , 0.5f, 1f
    )
    animator.duration = 500
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.start()
}

class JellyInterpolator(var factor: Float = 0.15f) : LinearInterpolator() {

    override fun getInterpolation(input: Float): Float {

        return ((Math.pow(
            2.0,
            (-10 * input).toDouble()
        ) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1).toFloat())
    }

}

fun <T> Single<T>.subscribeDbResult(
    onSuccess: (data: T) -> Unit,
    onFailed: (e: Throwable) -> Unit
) {
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                onFailed(e)
            }


        })
}


val SP_USER_INFO: String = "sp_user_info"
val KEY_IS_LOGIN: String = "key_is_login"
val KEY_AVATAR_STRING: String = "key_avatar_string"

/*
val CATEGORY_ENTERING_SINGER = 5001
val COVER_ENTERING_SINGER = "https://p1.music.126.net/F9asgcj7C7qSl_je9XDvRw==/603631883675241.jpg"

val CATEGORY_CHINESE_MEN_SINGER = 1001
val CATEGORY_CHINESE_WOMEN_SINGER = 1002
val CATEGORY_CHINESE_GROUP_SINGER = 1003
val COVER_CHINESE_MEN_SINGER = "https://p1.music.126.net/Xl0WENt4F6wsgjjjQWuQsw==/109951164232034479.jpg"
val COVER_CHINESE_WOMEN_SINGER = "https://p2.music.126.net/r7uMnwjWpYMuQI_3ZTg56A==/18528969953189760.jpg"
val COVER_CHINESE_GROUP_SINGER = "https://p1.music.126.net/WSxQCBXPPI-41Qz77fLsOw==/109951162809092287.jpg"

val CATEGORY_WESTERN_MEN_SINGER = 2001
val CATEGORY_WESTERN_WOMEN_SINGER = 2002
val CATEGORY_WESTERN_GROUP_SINGER = 2003
val COVER_WESTERN_MEN_SINGER = "https://p2.music.126.net/AsrGD4Sf0HWJ3mK-Y__PLw==/18981968742063387.jpg"
val COVER_WESTERN_WOMEN_SINGER = "https://p1.music.126.net/nKfpcOrY03UoaBwTPOyVDg==/109951164219903323.jpg"
val COVER_WESTERN_GROUP_SINGER ="https://p2.music.126.net/0PJOX5d5lQ1iFO395aWsnQ==/109951163845700865.jpg"

val CATEGORY_JAPANESE_MEN_SINGER = 6001
val CATEGORY_JAPANESE_WOMEN_SINGER = 6002
val CATEGORY_JAPANESE_GROUP_SINGER = 6003
val COVER_JAPANESE_MEN_SINGER = "https://p1.music.126.net/28e5ODJRXYLhZ6s3seFoMQ==/109951164021265258.jpg"
val COVER_JAPANESE_WOMEN_SINGER ="https://p1.music.126.net/LJoFlsgnmADsBq-9KpLEEg==/109951163992209298.jpg"
val COVER_JAPANESE_GROUP_SINGER = "https://p1.music.126.net/8QqlAn1YA8h_xZ6Tlx5dgQ==/109951163805685684.jpg"

val CATEGORY_KOREA_MEN_SINGER = 7001
val CATEGORY_KOREA_WOMEN_SINGER = 7002
val CATEGORY_KOREA_GROUP_SINGER = 7003
val COVER_KOREA_MEN_SINGER = "https://p2.music.126.net/JVgT0Bq7zwXYcqvvyyotig==/109951163537429532.jpg"
val COVER_KOREA_WOMEN_SINGER ="https://p1.music.126.net/PNNB81Ph5r4LzgNIMmdVIw==/109951164333560289.jpg"
val COVER_KOREA_GROUP_SINGER = "https://p1.music.126.net/o3Tv7bqIaIBGOGoXzuj88g==/109951163992758796.jpg"

val CATEGORY_OTHER_MEN_SINGER = 4001
val CATEGORY_OTHER_WOMEN_SINGER = 4002
val CATEGORY_OTHER_GROUP_SINGER = 4003
val COVER_OTHER_MEN_SINGER = "https://p1.music.126.net/p5r5d2JU-2U9YjgPArL5ag==/109951163423956072.jpg"
val COVER_OTHER_WOMEN_SINGER ="https://p2.music.126.net/slAqQ-CKVbxH-vJ9Nq3UOg==/109951163921647634.jpg"
val COVER_OTHER_GROUP_SINGER = "https://p1.music.126.net/dkUARil7aOnlU7B0t4-wgg==/1380986610868162.jpg"
*/

fun getJson(context: Context, filename: String): String {
    val stringBuilder = StringBuilder()
    val assetManager = context.assets
    try {
        val bufferedReader = BufferedReader(InputStreamReader(assetManager.open(filename), "utf-8"))
        val allText = bufferedReader.use(BufferedReader::readText)
        stringBuilder.append(allText)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}

fun <T> JsonToObject(json: String, type: Class<T>): T {
    val gson = Gson()
    return gson.fromJson(json,type)
}





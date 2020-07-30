package com.brins.lightmusic.utils

import android.animation.*
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.icu.util.Calendar
import android.media.MediaMetadataRetriever
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.TextUtils
import android.text.format.Time
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.onlinemusic.MusicCommentResult
import com.brins.lightmusic.ui.widget.CommentPopup
import com.brins.lightmusic.ui.widget.DimView
import com.brins.lightmusic.ui.widget.ProgressLoading
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.lxj.xpopup.XPopup
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
    if (url.isEmpty()) {
        return string2Bitmap(null)!!
    }
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


fun string2Bitmap(bitmapString: String?): Bitmap? {
    var bitmap: Bitmap? = null
    if (bitmapString != null) {
        try {
            val b = Base64.decode(bitmapString, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                b, 0,
                b.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
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


class SpacesItemDecoration(var mContext: Context, var space: Int, var drawableId: Int) :
    RecyclerView.ItemDecoration() {

    val mDrawable = ContextCompat.getDrawable(this.mContext, drawableId)
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            drawHorizontalDecoration(c, parent.getChildAt(i))
        }
    }

    private fun drawHorizontalDecoration(c: Canvas, childAt: View) {
        val rect = Rect(0, 0, 0, 0)
        rect.top = childAt.bottom
        mDrawable?.let {
            rect.bottom = rect.top + space
            rect.left = childAt.left
            rect.right = childAt.right
            it.bounds = rect
            it.draw(c)
        }

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
val KEY_COOKIE: String = "key_cookie"

val MAINLAND = "内地"
val HONGKONG_TAIWAN = "港台"
val EUROPE_AMERICA = "欧美"
val JAPAN = "日本"
val KOREA = "韩国"

val SEARCH_KEY = "search_key"


enum class SearchType(val type: Int) {

    SONG(10010),

    MV(10086),

    ALBUM(10000),

    MUSIC(1),

    ALBUMS(10),

    ARTIST(100),

    MUSICLIST(1000),

    MUSICVIDEO(1004)

}

val TYPE_ONLINE_MUSIC = 1
val TYPE_LOCAL_MUSIC = 0

val TYPE_MESSAGE = 0
val TYPE_FRIEND = 1
val TYPE_THEME = 3
val TYPE_DONATE = 4
val TYPE_ABOUT = 5

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
    return gson.fromJson(json, type)
}

fun getCalendarDay(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    } else {
        val time = Time()
        time.setToNow()
        time.monthDay.toString()
    }
}


fun dp2px(context: Context, value: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}

fun px2dp(context: Context, value: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (value / scale + 0.5f).toInt()
}

fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = CoroutineScope(
    Dispatchers.Main
).launch {
    try {
        block()
    } catch (e: Throwable) {
        try {
            error(e)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                Log.d("await", response.message())
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response body is null"))
            }
        })
    }
}

class BezierEvaluator(var mFlagPoint: PointF) : TypeEvaluator<PointF> {


    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        return CalculateBezierPointForCubic(
            fraction,
            startValue,
            mFlagPoint,
            endValue,
            PointF(200f, 2000f)
        )
    }

}

/**
 * 二阶贝塞尔曲线B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
 *
 * @param t  曲线长度比例
 * @param p0 起始点
 * @param p1 控制点
 * @param p2 终止点
 * @return t对应的点
 */
fun CalculateBezierPointForQuadratic(t: Float, p0: PointF, p1: PointF, p2: PointF): PointF {
    val point = PointF()
    val temp = 1 - t
    point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x
    point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y
    return point

}

/**
 * 三阶贝塞尔曲线B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
 *
 * @param t  曲线长度比例
 * @param p0 起始点
 * @param p1 控制点1
 * @param p2 控制点2
 * @param p3 终止点
 * @return t对应的点
 */
fun CalculateBezierPointForCubic(t: Float, p0: PointF, p1: PointF, p2: PointF, p3: PointF): PointF {
    val point = PointF()
    val temp = 1 - t
    point.x =
        p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t
    point.y =
        p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t
    return point

}


fun createMask(activity: Activity, target: View): DimView {
    val linearLayout = DimView(activity, view = target)//新建一个LinearLayout
    linearLayout.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    return linearLayout
}

fun setMask(ac: Activity, target: View): View {
    val rect = Rect()
    val point = Point()
    target.setLayerType(LAYER_TYPE_SOFTWARE, null)
    target.getGlobalVisibleRect(rect, point)
    return createMask(ac, target)
}

fun setTranslucent(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // 设置状态栏透明
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 设置根布局的参数
        /*            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);*/
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun setColorTranslucent(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // 设置状态栏透明
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 设置根布局的参数
        /*            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);*/
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun getStatusBarHeight(context: Context): Int {
    // 获得状态栏高度
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}


class StarterCommon(var activity: Activity?) {
    private var mProgressLoading: ProgressLoading? = null
    private var mUnBackProgressLoading: ProgressLoading? = null
    var isProgressShow: Boolean = false
        private set


    fun onDestroy() {
        mProgressLoading = null
        mUnBackProgressLoading = null
        activity = null
    }

    private fun isFinishing(): Boolean {
        return activity == null || activity!!.isFinishing
    }


    fun showProgressLoading(resId: Int) {
        if (!isFinishing()) {
            showProgressLoading(activity!!.getString(resId))
        }
    }

    fun showProgressLoading(text: String) {
        if (mProgressLoading == null) {
            mProgressLoading = ProgressLoading(activity!!, R.style.ProgressLoadingTheme)
            mProgressLoading!!.setCanceledOnTouchOutside(true)
            mProgressLoading!!.setOnCancelListener(DialogInterface.OnCancelListener {
                isProgressShow = false
            })
        }
        if (!TextUtils.isEmpty(text)) {
            mProgressLoading!!.mText = text
        } else {
            mProgressLoading!!.mText = ""
        }
        isProgressShow = true
        mProgressLoading!!.show()
    }

    fun dismissProgressLoading() {
        if (mProgressLoading != null && !isFinishing()) {
            isProgressShow = false
            mProgressLoading!!.dismiss()
        }
    }

    fun showUnBackProgressLoading(resId: Int) {
        showUnBackProgressLoading(activity!!.getString(resId))
    }

    // 按返回键不可撤销的
    fun showUnBackProgressLoading(text: String) {
        if (mUnBackProgressLoading == null) {
            mUnBackProgressLoading =
                object : ProgressLoading(activity!!, R.style.ProgressLoadingTheme) {
                    override fun onBackPressed() {
                        super.onBackPressed()
                    }
                }
        }
        if (!TextUtils.isEmpty(text)) {
            mUnBackProgressLoading!!.mText = text
        } else {
            mUnBackProgressLoading!!.mText = ""
        }

        mUnBackProgressLoading!!.show()
    }

    fun dismissUnBackProgressLoading() {
        if (mUnBackProgressLoading != null && !isFinishing()) {
            mUnBackProgressLoading!!.dismiss()
        }
    }
}

/**
 * @param view              共享的控件
 * @param sharedElementName 共享名字
 */
fun ActivityTransitionAnimation(
    context: Activity,
    intent: Intent,
    view: View, sharedElementName: String = AppConfig.SHARE_ANIMATION_IMAGE_NAME

) {
    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context,
        view,
        sharedElementName
    )
    context.startActivity(intent, optionsCompat.toBundle())
}

fun ActivityTransitionAnimation(
    context: Activity,
    intent: Intent,
    view: View, sharedElementName: String,
    requestCode: Int
) {
    view.transitionName = sharedElementName
    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
        context,
        view,
        AppConfig.SHARE_ANIMATION_IMAGE_NAME
    )
    context.startActivityForResult(intent, requestCode, optionsCompat.toBundle())
}

//高斯模糊
fun rsBlur(context: Context, source: Bitmap, radius: Float): Bitmap {

    var inputBmp = source
    //(1)
    val renderScript = RenderScript.create(context)

    Log.i("Render", "scale size:" + inputBmp.width + "*" + inputBmp.height)

    // Allocate memory for Renderscript to work with
    //(2)
    val input = Allocation.createFromBitmap(renderScript, inputBmp)
    val output = Allocation.createTyped(renderScript, input.type)
    //(3)
    // Load up an instance of the specific script that we want to use.
    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    //(4)
    scriptIntrinsicBlur.setInput(input)
    //(5)
    // Set the blur radius
    scriptIntrinsicBlur.setRadius(radius)
    //(6)
    // Start the ScriptIntrinisicBlur
    scriptIntrinsicBlur.forEach(output)
    //(7)
    // Copy the output to the blurred bitmap
    output.copyTo(inputBmp)
    //(8)
    renderScript.destroy()

    return inputBmp
}


/**
 * 正则：手机号（简单）
 */
val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"
/**
 * 正则：手机号（精确）
 *
 * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
 *
 * 联通：130、131、132、145、155、156、171、175、176、185、186
 *
 * 电信：133、153、173、177、180、181、189
 *
 * 全球星：1349
 *
 * 虚拟运营商：170
 */
val REGEX_MOBILE_EXACT =
    "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,1,3,5-8])|(18[0-9])|(147))\\d{8}$"


/**
 * 验证手机号（简单）
 *
 * @param input 待验证文本
 * @return `true`: 匹配<br></br>`false`: 不匹配
 */
fun isMobileSimple(input: CharSequence): Boolean {
    return isMatch(REGEX_MOBILE_SIMPLE, input)
}

/**
 * 验证手机号（精确）
 *
 * @param input 待验证文本
 * @return `true`: 匹配<br></br>`false`: 不匹配
 */
fun isMobileExact(input: CharSequence): Boolean {
    return isMatch(REGEX_MOBILE_EXACT, input)
}


/**
 * 判断是否匹配正则
 *
 * @param regex 正则表达式
 * @param input 要匹配的字符串
 * @return `true`: 匹配<br></br>`false`: 不匹配
 */
fun isMatch(regex: String, input: CharSequence?): Boolean {
    return input != null && input.length > 0 && Pattern.matches(regex, input)
}


//创建线性渐变背景色
fun createLinearGradientBitmap(darkColor: Int, color: Int, width: Int, height: Int): Bitmap? {
    val bgColors = IntArray(3)
    bgColors[0] = Color.parseColor("#00000000")
    bgColors[1] = Color.parseColor("#4d000000")
    bgColors[2] = color

    val bgBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
    val canvas = Canvas()
    val paint = Paint()
    canvas.setBitmap(bgBitmap)
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    val gradient =
        LinearGradient(0f, 0f, 0f, bgBitmap.height.toFloat(), bgColors, null, Shader.TileMode.CLAMP)
    paint.shader = gradient
    paint.isAntiAlias = true
    val rectF = RectF(0f, 0f, bgBitmap.width.toFloat(), bgBitmap.height.toFloat())
    canvas.drawRoundRect(rectF, 20f, 20f, paint)
    canvas.drawRect(rectF, paint)
    return bgBitmap
}

fun handleBimap(bitmap: Bitmap): Bitmap {
    //透明渐变
    val argb = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(argb, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    //循环开始的下标，设置从什么时候开始改变
    val start = argb.size / 2
    val end = argb.size

    //        int mid = argb.length;
    //        int row = ((mid - start) / bitmap.getHeight()) + 2;


    val width = bitmap.width
    for (i in 0 until bitmap.height / 2 + 1) {
        for (j in 0 until width) {
            val index = start - width + i * width + j
            if (argb[index] != 0) {
                argb[index] =
                    ((1 - i / (bitmap.height / 2f)) * 255).toInt() shl 24 or (argb[index] and 0x00FFFFFF)
            }
        }
    }
    //        for (int i = mid; i < argb.length; i++) {
    //            argb[i] = (argb[i] & 0x00FFFFFF);
    //        }

    return Bitmap.createBitmap(argb, bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

}


fun showRecommentList(context : Context, listBeans: List<MusicCommentResult.Companion.HotComments>): CommentPopup {
    val mCommentPopup = CommentPopup(context, listBeans)
    XPopup.Builder (context)
        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
        .hasShadowBg(false)
        .hasStatusBarShadow(false)
        .asCustom(mCommentPopup)
        .show()
    return mCommentPopup
}

fun setTextDark(window: Window, isDark: Boolean) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility;
        if (isDark) {
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun handleNum(num: Int): String {
    return if (num > 10000) {
        "${num / 10000}万"
    } else num.toString()
}
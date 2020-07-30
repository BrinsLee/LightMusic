package com.brins.lightmusic.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import kotlin.math.min
import kotlin.math.roundToInt


class FadingScrollView constructor(context: Context, attributeSet: AttributeSet) :
    NestedScrollView(context, attributeSet) {

    val TAG = "FadingScrollView"
    var fadingView: View? = null
    var fadingHeightView: View? = null
    private var fadingHeight = 500
    private var mBgDrawable: Drawable? = null


    fun setDrawable(bitmap: Bitmap) {
        mBgDrawable = BitmapDrawable(resources, bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (fadingHeightView != null) {
            fadingHeight = fadingHeightView!!.measuredHeight
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //        l,t  滑动后 xy位置，
//        oldl lodt 滑动前 xy 位置-----
        val fading = (if (t > fadingHeight) fadingHeight else if (t > 20) 2 * t else 0).toFloat()
        updateActionBarAlpha(min(fading / fadingHeight * 255, 255f))
    }

    private fun updateActionBarAlpha(alpha: Float) {
        try {
            setActionBarAlpha(alpha)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    fun setActionBarAlpha(alpha: Float) {
        if (fadingView != null && mBgDrawable != null) {
            mBgDrawable!!.alpha = alpha.roundToInt()
            Log.d(TAG, "${alpha.roundToInt()}")
            fadingView!!.background = mBgDrawable
        }
    }
}
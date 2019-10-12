package com.brins.lightmusic.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

class FadingScrollView constructor(context: Context , attributeSet: AttributeSet): NestedScrollView(context,attributeSet) {

    val TAG = "FadingScrollView"
    var fadingView : View? = null
    var fadingHeightView : View? = null
    private var fadingHeight = 500

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (fadingHeightView != null){
            fadingHeight = fadingHeightView!!.measuredHeight
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //        l,t  滑动后 xy位置，
//        oldl lodt 滑动前 xy 位置-----
        val fading = (if (t > fadingHeight) fadingHeight else if (t > 20) 2 * t else 0).toFloat()
        updateActionBarAlpha(fading / fadingHeight)
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
        if (fadingView == null) {
            throw Exception("fadingView is null...")
        }
        fadingView!!.alpha = alpha
    }
}
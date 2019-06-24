package com.brins.lightmusic.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class MainViewPager @JvmOverloads constructor(context: Context, attrs : AttributeSet) : ViewPager(context,attrs) {

    private var isCanScroll = true

    fun setIsScanScroll(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        if (v.javaClass.name == "com.brins.lightsong.customview.PileLayout"){
            return true
        }
        return super.canScroll(v, checkV, dx, x, y)
    }
}
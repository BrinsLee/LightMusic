package com.brins.lightmusic.ui.customview

import android.content.Context
import cn.jzvd.JzvdStd
import com.brins.lightmusic.R


class JZVideoPalyerView @JvmOverloads constructor(
    context: Context,
    attrs: android.util.AttributeSet
) : JzvdStd(context, attrs) {


    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }


}
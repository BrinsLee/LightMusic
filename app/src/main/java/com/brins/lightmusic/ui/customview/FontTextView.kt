package com.brins.lightmusic.ui.customview

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.TypefaceUtils
import android.util.AttributeSet as AttributeSet1

class FontTextView @JvmOverloads constructor(
    context: Context,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FontTextView)
        val fontType = attributes.getInteger(R.styleable.FontTextView_font_type, 0)
        attributes.recycle()
        initTypeface(context, fontType)
    }


    private fun initTypeface(context: Context, fontType: Int) {
        if (fontType > 0) {
            val typeface = TypefaceUtils.Companion.getTypeface(context, fontType)
            if (typeface != null) {
                setTypeface(typeface)
            }
        }
    }

}
package com.brins.lightmusic.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.widget.AppCompatTextView
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.getTypeface
import android.util.AttributeSet as AttributeSet1

class FontTextView @JvmOverloads constructor(
    context: Context,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {
    val startColor : Int
    val endColor : Int
    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FontTextView)
        val fontType = attributes.getInteger(R.styleable.FontTextView_font_type, 0)
        startColor = attributes.getColor(R.styleable.FontTextView_start_color, 0)
        endColor = attributes.getColor(R.styleable.FontTextView_end_color , 0)
        attributes.recycle()
        initTypeface(context, fontType)
    }


    private fun initTypeface(context: Context, fontType: Int) {
        if (fontType > 0) {
            val typeface = getTypeface(context, fontType)
            if (typeface != null) {
                setTypeface(typeface)
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed && startColor != 0 && endColor != 0) {//RadialGradient(float x, float y, float radius, int[] colors, float[] positions, Shader.TileMode tile)
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                    startColor,
                    endColor
                , Shader.TileMode.CLAMP
            )
        }
    }

}
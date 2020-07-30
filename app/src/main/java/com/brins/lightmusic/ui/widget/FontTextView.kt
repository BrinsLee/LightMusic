package com.brins.lightmusic.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import androidx.appcompat.widget.AppCompatTextView
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.getTypeface
import android.widget.TextView
import androidx.annotation.ColorInt
import java.lang.Exception
import java.lang.reflect.Field
import android.text.TextPaint


class FontTextView @JvmOverloads constructor(
    context: Context,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {
    val startColor: Int
    val endColor: Int
    val stoke: Boolean

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FontTextView)
        val fontType = attributes.getInteger(R.styleable.FontTextView_font_type, 0)
        startColor = attributes.getColor(R.styleable.FontTextView_start_color, 0)
        endColor = attributes.getColor(R.styleable.FontTextView_end_color, 0)
        stoke = attributes.getBoolean(R.styleable.FontTextView_stroke, false)
        attributes.recycle()
        initTypeface(context, fontType)
    }

    @ColorInt
    private val mBorderColor = Color.GRAY
    @ColorInt
    private val mInnerColor = Color.WHITE
    private val mTextPaint: TextPaint = this.paint


    private fun initTypeface(context: Context, fontType: Int) {
        if (fontType > 0) {
            val typeface = getTypeface(context, fontType)
            if (typeface != null) {
                setTypeface(typeface)
            }
        }
    }

    override fun isFocused(): Boolean {
        return true
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

    override fun onDraw(canvas: Canvas?) {
        if (stoke) {
            setTextColorUseReflection(mBorderColor)
            paint.strokeWidth = 3f
            paint.style = Paint.Style.STROKE
            paint.isFakeBoldText = true
            paint.setShadowLayer(1f, 0f, 0f, 0)
            super.onDraw(canvas)

            setTextColorUseReflection(mInnerColor)
            mTextPaint.strokeWidth = 0f
            mTextPaint.style = Paint.Style.FILL
            mTextPaint.isFakeBoldText = false

        }
        super.onDraw(canvas)

    }

    private fun setTextColorUseReflection(color: Int) {
        var textColorField: Field
        try {
            textColorField = TextView::class.java.getDeclaredField("mCurTextColor")
            textColorField.isAccessible = true
            textColorField.set(this, color)
            textColorField.isAccessible = false

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mTextPaint.color = color
    }
}
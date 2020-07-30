package com.brins.lightmusic.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.airbnb.lottie.LottieProperty

class RoundCoverProgress  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ImageView(context, attrs) {

    private var centerY = 0
    private var centerX = 0
    private var circleRadius: Float = 0f
    private var mLastAnimationValue: Long = 0
    private val mBorderRect = RectF()
    private val mBorderPaint = Paint()
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private val mBorderPaint1 = Paint()
    private val mBorderRect1 = RectF()

    var newAngle: Float = 0f // 画弧线的角度
        set(value) {
            field = value
            invalidate()
        }

    companion object {
        private var DEFAULT_BORDER_WIDTH = 10f
        private var DEFAULT_BORDER_COLOR = Color.parseColor("#29a2fb")
    }

    init {
        mBorderPaint1.strokeWidth = LottieProperty.POLYSTAR_INNER_ROUNDEDNESS
        mBorderPaint1.style = Paint.Style.STROKE
        mBorderPaint1.color = mBorderColor
        mBorderPaint1.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circleRadius = (width - 10) / 2f
        mBorderWidth = circleRadius / 12f
        centerX = width / 2
        centerY = height / 2
        mBorderRect1.set(
            centerX - circleRadius, centerY - circleRadius, centerX + circleRadius,
            centerY + circleRadius
        )

        canvas.drawArc(mBorderRect1, -90f, newAngle, false, mBorderPaint1)

    }
}
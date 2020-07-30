package com.brins.lightmusic.ui.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.makeramen.roundedimageview.RoundedImageView
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.R


class RoundCoverImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {
    /*    private var centerY = 0
        private var centerX = 0
        private var circleRadius: Float = 0f*/
    private var mRotateAnimator: ObjectAnimator
    private var mLastAnimationValue: Long = 0
    private var mImageView: RoundedImageView
    private var mProgress: RoundCoverProgress
    private var mMax = 360
        get() = field

/*    private val mBorderRect = RectF()
    private val mBorderPaint = Paint()
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private val mBorderPaint1 = Paint()
    private val mBorderRect1 = RectF()

    var newAngle: Float = 50f // 画弧线的角度


    companion object {
        private var DEFAULT_BORDER_WIDTH = 10f
        private var DEFAULT_BORDER_COLOR = Color.parseColor("#29a2fb")
    }*/

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_bottom_cover, this)
        mImageView = view.findViewById(R.id.bottom_cover)
        mProgress = view.findViewById(R.id.cover_progress)
        mRotateAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f)
        mRotateAnimator.duration = 7200
        mRotateAnimator.interpolator = LinearInterpolator()
        mRotateAnimator.repeatMode = ValueAnimator.RESTART
        mRotateAnimator.repeatCount = ValueAnimator.INFINITE
/*        mBorderPaint1.strokeWidth = POLYSTAR_INNER_ROUNDEDNESS
        mBorderPaint1.style = Paint.Style.STROKE
        mBorderPaint1.color = mBorderColor
        mBorderPaint1.isAntiAlias = true*/
    }


/*    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circleRadius = mImageView.width / 2f
        mBorderWidth = circleRadius / 12f
        centerX = mImageView.width / 2
        centerY = mImageView.height / 2
        mBorderRect1.set(
            centerX - circleRadius, centerY - circleRadius, centerX + circleRadius,
            centerY + circleRadius
        )

        canvas.drawArc(mBorderRect1, -90f, 100f, false, mBorderPaint1)

    }*/

    fun startRotateAnimation() {
        mRotateAnimator.cancel()
        mRotateAnimator.start()
    }

    fun cancelRotateAnimation() {
        mLastAnimationValue = 0
        mRotateAnimator.cancel()
    }

    fun pauseRotateAnimation() {
        mLastAnimationValue = mRotateAnimator.currentPlayTime
        mRotateAnimator.cancel()
    }

    fun resumeRotateAnimation() {
        mRotateAnimator.start()
        mRotateAnimator.currentPlayTime = mLastAnimationValue
    }

    fun setImageBitmap(bitmapCover: Bitmap?) {
        mImageView.setImageBitmap(bitmapCover)
    }

    fun getMax(): Int {
        return mMax
    }

    fun setProgress(initProgress: Int) {
        mProgress.newAngle = initProgress.toFloat()
    }
}
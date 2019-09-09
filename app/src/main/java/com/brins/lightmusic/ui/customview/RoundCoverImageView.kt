package com.brins.lightmusic.ui.customview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.makeramen.roundedimageview.RoundedImageView

class RoundCoverImageView @JvmOverloads constructor(context: Context, attrs : AttributeSet? = null): RoundedImageView(context , attrs) {
    private var mRotateAnimator: ObjectAnimator
    private var mLastAnimationValue: Long = 0
    init {
        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
        mRotateAnimator.duration = 7200
        mRotateAnimator.interpolator = LinearInterpolator()
        mRotateAnimator.repeatMode = ValueAnimator.RESTART
        mRotateAnimator.repeatCount = ValueAnimator.INFINITE
    }

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
}
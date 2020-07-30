package com.brins.lightmusic.ui.customview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.ScreenUtils
import com.brins.lightmusic.utils.dp2px
import kotlinx.android.synthetic.main.view_float_control.view.*
import kotlin.math.absoluteValue

/**
 * @author lipeilin
 * @date 2020/7/22
 */
class FloatControlView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : ConstraintLayout(context, attributeSet, def) {

    companion object {

        val TAG = "FloatControlView"
        val CANCEL_INTERVAL_DEFAULT = 3000
        val GO_TO_BOUNDARY_INTERVAL_DEFAULT = 100
        val IN_DISTANCE_DEFAULT = 5
        val IN_LENGTH_DEFAULT = 10

        // 默认控件活动边界留白
        private const val BLANK_LEFT_DEFAULT = 10
        private const val BLANK_RIGHT_DEFAULT = 10
        private const val BLANK_TOP_DEFAULT = 10
        private const val BLANK_BOTTOM_DEFAULT = 10


        /**
         * 该控件的三种模式,只要触发了ACTION_MOVE就是MOVE模式
         * 没有触发ACTION_MOVE但是从ACTION_DOWN开始超过了500ms就是CANCEL模式
         * 未超过就是CLICK模式
         */

        private const val MODE_CANCEL = 100
        private const val MODE_CLICK = 101
        private const val MODE_MOVE = 102
        private const val MODE_NONE = 103
        private const val MODE_MOVE_CLICK = 104

        @IntDef(value = [MODE_CANCEL, MODE_CLICK, MODE_MOVE, MODE_NONE, MODE_MOVE_CLICK])
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        private annotation class Mode

        /**
         * view所处的位置
         * 上下边界暂时不考虑
         */
        private const val POSITION_LEFT = 1000
        private const val POSITION_RIGHT = 1001
        private const val POSITION_FLYING = 1002

        @IntDef(value = [POSITION_LEFT, POSITION_RIGHT, POSITION_FLYING])
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        private annotation class Position
    }

    // 上一次的触摸点的位置
    private var mLastX = 0
    private var mLastY = 0
    private var mDisX = 0
    private var mDisY = 0
    private var mStartX = 0
    private var mStartY = 0
    private var mDownTime: Long = 0

    // 一个事件序列结束时的时间，就是这个事件序列ACTION_UP时的时间
    private var mUpTime: Long = 0

    // 边界属性, 单位是pixel
    private var mBlankLeft: Int = dp2px(context, BLANK_LEFT_DEFAULT.toFloat())
    private var mBlankRight = dp2px(context, BLANK_RIGHT_DEFAULT.toFloat())
    private var mBlankTop = dp2px(context, BLANK_TOP_DEFAULT.toFloat())
    private var mBlankBottom = dp2px(context, BLANK_BOTTOM_DEFAULT.toFloat())

    private val mScreenWidthInPixel = ScreenUtils.getScreenWidth(context)
    private val mScreenHeightInPixel = ScreenUtils.getScreenHeight(context)

    @Mode
    private var mMode: Int = MODE_NONE

    @Position
    private var mPosition = POSITION_LEFT

    private var mAlreadyAdjust = false

    // 是否正在动画
    private var mIsInAnimation = false

    // 当前是否是圆形
    private var mIsClosed = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_float_control, this)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x = event!!.rawX.toInt()
        var y = event.rawY.toInt()


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownTime = System.currentTimeMillis()
                mDisX = event.x.toInt()
                mDisY = event.y.toInt()
                mStartX = event.rawX.toInt()
                mStartY = event.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {

                // 重置这个结束的时间
                mUpTime = System.currentTimeMillis()

                // 设置当前的模式
                val dx = (mStartX - mLastX).absoluteValue
                val dy = (mStartY - mLastY).absoluteValue

                Log.d(TAG, "mDX: $dx")
                Log.d(TAG, "mDY: $dy")

                mMode = if (dx < 10 && dy < 10) {
                    MODE_MOVE_CLICK
                } else {
                    MODE_MOVE
                }

                if (mMode == MODE_CLICK || mMode == MODE_MOVE_CLICK) {
                    performClick()
                }
                if (mMode != MODE_CLICK) {
                    if (x > mBlankLeft && x < mScreenWidthInPixel - mBlankRight && mPosition != POSITION_LEFT && mPosition != POSITION_RIGHT) {
                        if (event.rawX < (mScreenWidthInPixel * 1.0 / 2).toFloat()) {

                            // 回到最左侧
                            val animator =
                                ObjectAnimator.ofFloat(
                                    this,
                                    "TranslationX",
                                    translationX,
                                    translationX + -1 * (x - mDisX - mBlankLeft)
                                )
                            animator.duration = GO_TO_BOUNDARY_INTERVAL_DEFAULT.toLong()
                            animator.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    mPosition = POSITION_LEFT
                                }

                                override fun onAnimationStart(
                                    animation: Animator?,
                                    isReverse: Boolean
                                ) {
                                    mPosition = POSITION_FLYING
                                }
                            })

                            animator.start()
                        } else {
                            // 回到最右侧

                            // 回到最右侧
                            val animator =
                                ObjectAnimator.ofFloat(
                                    this,
                                    "TranslationX",
                                    translationX,
                                    translationX + (mScreenWidthInPixel - mBlankRight - (width - mDisX + x))
                                )
                            animator.duration = GO_TO_BOUNDARY_INTERVAL_DEFAULT.toLong()
                            animator.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    mPosition = POSITION_RIGHT
                                }

                                override fun onAnimationStart(
                                    animation: Animator?,
                                    isReverse: Boolean
                                ) {
                                    mPosition = POSITION_FLYING
                                }
                            })

                            animator.start()
                        }
                    }
                }
                mMode = MODE_NONE
            }

            MotionEvent.ACTION_MOVE -> {

                mMode = MODE_MOVE

                var dx = 0
                var dy = 0

                // 预测量的边距
                val preXLeft = x - mDisX
                val preXRight = mScreenWidthInPixel - (x + width - mDisX)
                val preYUp = y - mDisY
                val preYDown: Int =
                    mScreenHeightInPixel - (y + width.coerceAtMost(height) - mDisY)

                when {
                    preXLeft <= mBlankLeft -> {
                        // 超出左边界
                        mPosition = POSITION_LEFT
                        dx = x - mLastX + mBlankLeft - preXLeft
                        x = x + mBlankLeft - preXLeft
                    }
                    preXRight <= mBlankRight -> {
                        // 超出右边界
                        mPosition = POSITION_RIGHT
                        dx = x - mLastX - (mBlankRight - preXRight)
                        x -= (mBlankRight - preXRight)
                    }
                    else -> {
                        // 正常
                        mPosition = POSITION_FLYING
                        dx = x - mLastX
                    }
                }

                // 处理Y坐标
                when {
                    preYUp <= mBlankTop -> {
                        // 超出上边界
                        dy = y - mLastY + mBlankTop - preYUp
                        y = y + mBlankTop - preYUp
                    }
                    preYDown <= mBlankBottom -> {
                        // 超出下边界
                        dy = y - mLastY - (mBlankBottom - preYDown)
                        y -= (mBlankBottom - preYDown)
                    }
                    else -> {
                        // 正常
                        dy = y - mLastY
                    }
                }

                translationX += dx
                translationY += dy
            }

        }
        // 更新位置
        mLastX = x
        mLastY = y
        return true
    }

    fun getMax(): Int {
        return cover.getMax()
    }

    fun setProgress(initProgress: Int) {
        cover.setProgress(initProgress)
    }

    fun pauseRotateAnimation() {
        cover.pauseRotateAnimation()
    }

    fun resumeRotateAnimation() {
        cover.resumeRotateAnimation()
    }

    fun startRotateAnimation() {
        cover.startRotateAnimation()
    }

    fun cancelRotateAnimation() {
        cover.cancelRotateAnimation()
    }

    fun setImageBitmap(bitmapCover: Bitmap?) {
        cover.setImageBitmap(bitmapCover)
    }
}
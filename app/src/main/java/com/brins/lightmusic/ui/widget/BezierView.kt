package com.brins.lightmusic.ui.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import com.brins.lightmusic.R
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.utils.BezierEvaluator
import kotlinx.android.synthetic.main.view_music_play.view.*


class BezierView @JvmOverloads constructor(context: Context,
                     attrs: AttributeSet? = null,
                     defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var mStartPointX : Float = 0f
    private var mStartPointY : Float = 0f

    private var mEndPointX : Float = 0f
    private var mEndPointY : Float = 0f

    private var mFlagPointX: Float = 0f
    private var mFlagPointY: Float = 0f

    private var mPath: Path? = null
    private var mPaintPath: Paint? = null
    private var mPaintCircle: Paint? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_music_play,this)
        mPath = Path()
        mPaintPath = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintPath!!.style = Paint.Style.STROKE
        mPaintPath!!.strokeWidth = 8f
        mPaintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
        mStartPointX = 100f
        mStartPointY = 800f

        mEndPointX = 600f
        mEndPointY = 1200f

        mFlagPointX = 500f
        mFlagPointY = 0f
        start()
    }

    fun start(){
        val evaluator = BezierEvaluator(PointF(mFlagPointX, mFlagPointY))
        val animator = ObjectAnimator.ofObject(evaluator,PointF(mStartPointX,mStartPointY),PointF(mEndPointX,mEndPointY))
        animator.duration = 2000
        animator.repeatCount = 1000
        animator.addUpdateListener {
            val p :PointF = it.animatedValue as PointF
            leftBall.x = p.x
            leftBall.y = p.y

            rightBall.x = p.x
            rightBall.y = p.y
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}
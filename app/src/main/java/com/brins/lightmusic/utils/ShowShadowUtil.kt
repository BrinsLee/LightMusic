package com.brins.lightmusic.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat

class ShowShadowUtil(
    var mShape: Int, var mBgColor: IntArray,
    var mShadowRadius: Float,var shadowColor: Int = 0, var mShapeRadius: Float,
    var mOffsetX: Float, var mOffsetY: Float
) : Drawable() {

    companion object{
        val SHAPE_ROUND = 1
        val SHAPE_CIRCLE = 2
        fun setShadowDrawable(
            view: View,
            shapeRadius: Float,
            shadowColor: Int,
            shadowRadius: Float,
            offsetX: Float,
            offsetY: Float
        ) {
            val drawable = ShowShadowUtil.Companion.Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        fun setShadowDrawable(
            view: View,
            bgColor: Int,
            shapeRadius: Float,
            shadowColor: Int,
            shadowRadius: Float,
            offsetX: Float,
            offsetY: Float
        ) {
            val drawable = ShowShadowUtil.Companion.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        fun setShadowDrawable(
            view: View,
            shape: Int,
            bgColor: Int,
            shapeRadius: Float,
            shadowColor: Int,
            shadowRadius: Float,
            offsetX: Float,
            offsetY: Float
        ) {
            val drawable = ShowShadowUtil.Companion.Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }

        fun setShadowDrawable(
            view: View,
            bgColor: IntArray,
            shapeRadius: Float,
            shadowColor: Int,
            shadowRadius: Float,
            offsetX: Float,
            offsetY: Float
        ) {
            val drawable = ShowShadowUtil.Companion.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            ViewCompat.setBackground(view, drawable)
        }
        class Builder {
            private var mShape: Int = 0
            private var mShapeRadius: Float = 0f
            private var mShadowColor: Int = 0
            private var mShadowRadius: Float = 0f
            private var mOffsetX = 0f
            private var mOffsetY = 0f
            private var mBgColor: IntArray

            init {
                mShape = SHAPE_ROUND
                mShapeRadius = 12f
                mShadowColor = Color.parseColor("#4d000000")
                mShadowRadius = 18f
                mBgColor = IntArray(1)
                mBgColor[0] = Color.TRANSPARENT
            }

            fun setShape(mShape: Int): Builder {
                this.mShape = mShape
                return this
            }

            fun setShapeRadius(ShapeRadius: Float): Builder {
                this.mShapeRadius = ShapeRadius
                return this
            }

            fun setShadowColor(shadowColor: Int): Builder {
                this.mShadowColor = shadowColor
                return this
            }

            fun setShadowRadius(shadowRadius: Float): Builder {
                this.mShadowRadius = shadowRadius
                return this
            }

            fun setOffsetX(OffsetX: Float): Builder {
                this.mOffsetX = OffsetX
                return this
            }

            fun setOffsetY(OffsetY: Float): Builder {
                this.mOffsetY = OffsetY
                return this
            }

            fun setBgColor(BgColor: Int): Builder {
                this.mBgColor[0] = BgColor
                return this
            }

            fun setBgColor(BgColor: IntArray): Builder {
                this.mBgColor = BgColor
                return this
            }

            fun builder(): ShowShadowUtil {
                return ShowShadowUtil(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY)
            }
        }
    }

    var mShadowPaint = Paint()
    var mBgPaint = Paint()
    private lateinit var mRect: RectF

    init {
        mShadowPaint.color = Color.TRANSPARENT
        mShadowPaint.isAntiAlias = true
        mShadowPaint.setShadowLayer(mShadowRadius, mOffsetX, mOffsetY, shadowColor)
        mShadowPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        mBgPaint.isAntiAlias = true
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mRect = RectF(
            left + mShadowRadius - mOffsetX, top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
            bottom - mShadowRadius - mOffsetY
        )
    }

    override fun draw(canvas: Canvas) {
        if (mBgColor.size == 1) {
            mBgPaint.color = mBgColor[0]
        } else {
            mBgPaint.shader = LinearGradient(
                mRect.left, mRect.height() / 2, mRect.right
                , mRect.height() / 2, mBgColor, null, Shader.TileMode.CLAMP
            )
        }
        if (mShape == SHAPE_ROUND) {
            canvas.drawRoundRect(mRect, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mShadowPaint)
            canvas.drawRoundRect(mRect, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mBgPaint)
        } else {
            canvas.drawCircle(
                mRect.centerX(),
                mRect.centerY(),
                Math.min(mRect.width(), mRect.height()) / 2,
                mShadowPaint
            )
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mBgPaint)

        }
    }

    override fun setAlpha(alpha: Int) {
        mShadowPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}
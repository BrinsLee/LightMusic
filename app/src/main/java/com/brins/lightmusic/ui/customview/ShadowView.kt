package com.brins.lightmusic.ui.customview

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.brins.lightmusic.R

class ShadowView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, def: Int = 0): View(context,attributes,def) {
    private val TAG = "ShadowImageView"
    private var mPaint: Paint = Paint()
    /**
     * 模糊半径
     */
    private var shadow_fuzzy_radius = 25f
    /**
     * 阴影颜色
     */
    private var shadow_color = Color.GRAY
    /**
     * 阴影高度
     */
    private var shadow_height = 25f
    val SHADOW_UP = 1
    val SHADOW_DOWN = 2
    /**
     * 阴影位置
     */
    private var shadow_position = SHADOW_DOWN
    /**
     * 位置
     */

    /**
     * 阴影的矩形
     */
    private var rect: RectF? = null

    /**
     * 显示阴影的控件id
     */
    private var view_id: Int = 0
    private var view: View? = null
    init {
        val a = context.obtainStyledAttributes(attributes,R.styleable.CustomShadowView)
        initView(a)
    }



    private fun initView(typeArray: TypedArray) {
        initAttrs(typeArray)
        mPaint.color = shadow_color
        this.setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = -1f
        }
    }

    private fun initAttrs(typedArray: TypedArray?) {
        if (typedArray != null) {
            shadow_color =
                typedArray.getColor(R.styleable.CustomShadowView_shadow_color, Color.GRAY)
            shadow_height = typedArray.getDimension(R.styleable.CustomShadowView_shadow_height, 25f)
            shadow_position = typedArray.getInt(R.styleable.CustomShadowView_shadow_position, 2)
            shadow_fuzzy_radius =
                typedArray.getFloat(R.styleable.CustomShadowView_shadow_fuzzy_radius, 25f)
            view_id = typedArray.getResourceId(R.styleable.CustomShadowView_view_id, 0)
            typedArray.recycle()
        }
    }

    private fun findView() {
        if (view_id == 0) {
            return
        }
        val context = context
        if (context is Activity) {
            val activity = getContext() as Activity
            Log.e(TAG, "id是多少: $view_id")
            val view = activity.findViewById<View>(view_id)
            this.view = view
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shadow_position == SHADOW_UP) {//阴影在上面
            mPaint.setShadowLayer(shadow_fuzzy_radius, 0f, -shadow_height, shadow_color)
            rect = RectF(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                (bottom - (bottom - top) / 5).toFloat()
            )
        } else if (shadow_position == SHADOW_DOWN) {//阴影在下面
            mPaint.setShadowLayer(shadow_fuzzy_radius, 0f, shadow_height, shadow_color)
            rect = RectF(
                left.toFloat(),
                (top + (bottom - top) / 5).toFloat(),
                right.toFloat(),
                bottom.toFloat()
            )
        }
        canvas.drawRect(rect!!, mPaint)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (this.view != null) {
            this.left = this.view!!.left
            this.top = this.view!!.top
            this.right = this.view!!.right
            this.bottom = this.view!!.bottom
        }
        Log.e(TAG, "onMeasure: 宽:" + this.right + "高:" + this.bottom)
        setMeasuredDimension(this.right, this.bottom)
    }
}
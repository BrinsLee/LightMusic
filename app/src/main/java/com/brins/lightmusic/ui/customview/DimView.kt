package com.brins.lightmusic.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.brins.lightmusic.R

class DimView @JvmOverloads constructor(context: Context,attributeSet: AttributeSet? = null,defStyleAttr : Int = 0 , var view : View): LinearLayout(context, attributeSet,defStyleAttr) {

    private var mPaint = Paint()

    private lateinit var rectF: RectF

    init {
        initView()
    }

    private fun initView() {
        mPaint.setARGB(0, 255, 0, 0)
        mPaint.isAntiAlias = true
        val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mPaint.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER)
        mPaint.xfermode = xfermode
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
//        rectF = new RectF(35.0f,914.0f,1405.0f,1264.0f);
        rectF = RectF(
            view.left.toFloat(),
            view.top.toFloat(),
            view.right.toFloat(),
            view.bottom.toFloat()
        )
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(ContextCompat.getColor(context,R.color.mask))
        canvas.drawRoundRect(rectF, 12f, 12f, mPaint)

    }
}
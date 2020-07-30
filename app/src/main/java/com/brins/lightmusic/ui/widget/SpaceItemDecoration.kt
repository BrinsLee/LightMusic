package com.brins.lightmusic.ui.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpaceItemDecoration(var mDividerHight: Int, var color: Int) : RecyclerView.ItemDecoration() {
    private val TAG = SpaceItemDecoration::class.java.name
    private val mDividerHeight = 2
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG);
    //取名mDivider似乎更恰当
    private val mDrawable: Drawable? = null

    init {
        mPaint.color = color
        mPaint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = mDividerHeight
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (params.spanIndex % 2 === 0) {
            outRect.left = mDividerHeight
            outRect.right = mDividerHeight / 2
        } else {
            outRect.left = mDividerHeight / 2
            outRect.right = mDividerHeight
        }


    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawVerticalFirstRow(c, parent)
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDrawable != null) {
                mDrawable.setBounds(left, top, right, bottom)
                mDrawable.draw(c)
            }
            if (mPaint != null) {
                c.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint
                )
            }
        }
    }

    private fun drawVerticalFirstRow(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val left = parent.paddingLeft
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            //            final int left = child.getLeft() - layoutParams.leftMargin;
            val right = left + mDividerHeight
            if (mDrawable != null) {
                mDrawable.setBounds(left, top, right, bottom)
                mDrawable.draw(c)
            }
            if (mPaint != null) {
                c.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint
                )
            }
        }

    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val x = parent.paddingLeft
        val width = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            //item底部的Y轴坐标+margin值
            val y = child.bottom + layoutParams.bottomMargin
            val height = y + mDividerHeight
            Log.e("height", "$height===================")
            if (mDrawable != null) {
                //setBounds(x,y,width,height); x:组件在容器X轴上的起点 y:组件在容器Y轴上的起点
                // width:组件的长度 height:组件的高度
                mDrawable.setBounds(x, y, width, height)
                mDrawable.draw(c)
            }
            if (mPaint != null) {
                c.drawRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), mPaint)
            }
        }
    }
}
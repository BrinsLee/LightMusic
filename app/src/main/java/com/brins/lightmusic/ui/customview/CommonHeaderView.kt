package com.brins.lightmusic.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.lightmusic.R
import kotlinx.android.synthetic.main.view_common_header.view.*

class CommonHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var mOnBackClickListener: OnBackClickListener? = null
    var title: String = ""
        set(value) {
            setHeadTitle(value)
        }
    @ColorRes
    var textColor = R.color.white


    init {
        LayoutInflater.from(context).inflate(R.layout.view_common_header, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.CommonHeaderView)
        title = a.getString(R.styleable.CommonHeaderView_chv_title)
        textColor = a.getColor(
            R.styleable.CommonHeaderView_chv_text_color,
            resources.getColor(R.color.white, null)
        )
        val icon = a.getResourceId(R.styleable.CommonHeaderView_chv_icon, R.drawable.ic_back)
        headBack.setImageResource(icon)
        a.recycle()
        headBack.setOnClickListener {
            onBackClick(it)
        }
        initView(title)
    }


    fun initView(title: String?) {
        headTitle.text = title
        headTitle.setTextColor(textColor)
    }

    private fun setHeadTitle(value: String) {
        initView(value)
    }

    fun setOnBackClickListener(onBackClickListener: OnBackClickListener) {
        mOnBackClickListener = onBackClickListener
    }

    fun onBackClick(view: View) {
        if (mOnBackClickListener != null) {
            mOnBackClickListener!!.onBackClick(view)
        }
    }

    interface OnBackClickListener {
        fun onBackClick(view: View)
    }
}
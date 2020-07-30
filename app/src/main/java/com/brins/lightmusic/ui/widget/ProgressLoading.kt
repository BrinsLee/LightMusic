package com.brins.lightmusic.ui.widget

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import com.brins.lightmusic.R

open class ProgressLoading(context: Context, theme: Int) : Dialog(context, theme) {

    private var mProgressBar: ProgressBar? = null
    private var mTextView: TextView? = null

    var mText: CharSequence? = null
    var mShowProgress = true

    init {
        initialize()
    }

    private fun initialize() {
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.widget_progress)
        mTextView = findViewById(android.R.id.text1)
        mProgressBar = findViewById(android.R.id.progress)
    }

    fun updateText(text: CharSequence): ProgressLoading {
        mText = text
        show()
        return this
    }

    fun hideText(): ProgressLoading {
        return updateText("")
    }

    fun hideProgressBar(): ProgressLoading {
        mShowProgress = false
        show()
        return this
    }

    override fun show() {
        if (TextUtils.isEmpty(mText)) {
            mTextView?.visibility = View.GONE
        } else {
            mTextView?.visibility = View.VISIBLE
            mTextView?.text = mText
        }

        if (mShowProgress) {
            mProgressBar?.visibility = View.VISIBLE
        } else {
            mProgressBar?.visibility = View.GONE
        }
        removeFocus(this)

        super.show()
    }

    private fun removeFocus(context: ProgressLoading) {
        context.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
    }
}
package com.brins.lightmusic.ui.customview

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment : DialogFragment() {
    val TAG = this.javaClass.simpleName
    val DEFAULT_DIM_AMOUNT = 0.6f

    fun show(fragmentManager: FragmentManager): BaseDialogFragment {
        try {
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment != null && fragment.isAdded) {
                return fragment as BaseDialogFragment
            }
            val ft = fragmentManager.beginTransaction()
            ft.add(this, TAG)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                ft.commitNow()
            }else{
                ft.commit()
                fragmentManager.executePendingTransactions()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(getLayoutResId(), container, false)
        if (view != null) {
            onCreateViewAfterBinding(view)
        }
        if (isInterceptKeyCodeBack()) {
            dialog?.setOnKeyListener { _, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    private fun initParams() {
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                //去除背景
                window.setBackgroundDrawableResource(android.R.color.transparent)
                //设置布局属性
                val layoutParams = window.attributes
                layoutParams.width = getDialogWidth()
                layoutParams.height = getDialogHeight()
                layoutParams.dimAmount = getDimAmount()
                layoutParams.gravity = getGravity()
                window.attributes = layoutParams
                if (getDialogAnimResId() > 0) {
                    window.setWindowAnimations(getDialogAnimResId())
                }
            }
            //设置是否点击外面可以取消
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside())
        }
    }

    protected open fun onCreateViewAfterBinding(view: View) {

    }

    protected open fun isInterceptKeyCodeBack(): Boolean {
        return false
    }

    protected open fun isCanceledOnTouchOutside(): Boolean {
        return true
    }


    protected open fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected open fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected open fun getGravity(): Int {
        return Gravity.CENTER
    }

    protected open fun getDimAmount(): Float {
        return DEFAULT_DIM_AMOUNT
    }

    protected open fun getDialogAnimResId(): Int {
        return 0
    }


    protected abstract fun getLayoutResId(): Int
}
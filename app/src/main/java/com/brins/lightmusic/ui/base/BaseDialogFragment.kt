package com.brins.lightmusic.ui.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.Exception

abstract class BaseDialogFragment : DialogFragment() {

    val TAG = this::class.java.simpleName

    val DEFAULT_DIM_AMOUNT = 0.6f
    protected abstract fun getLayoutResID(): Int
    fun show(fragmentManager: FragmentManager): BaseDialogFragment {
        try {
            fragmentManager.executePendingTransactions()
            val fragment = fragmentManager.findFragmentByTag(TAG)
            if (fragment != null && fragment.isAdded) {
                return fragment as BaseDialogFragment
            }
            val ft = fragmentManager.beginTransaction()
            ft.add(this, TAG)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(getLayoutResID(), container, false)
        if (view != null) {
            onCreateViewAfterBinding(view)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        init()
    }



    private fun init() {
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            if (window != null) {
                //去除背景
                window.setBackgroundDrawableResource(android.R.color.transparent)
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
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside())

        }
    }

    protected open fun onCreateViewAfterBinding(view: View) {}
    protected open fun isCanceledOnTouchOutside(): Boolean {
        return true
    }

    protected open fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected open fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected fun getGravity(): Int {
        return Gravity.CENTER
    }

    protected fun getDimAmount(): Float {
        return DEFAULT_DIM_AMOUNT
    }

    protected fun getDialogAnimResId(): Int {
        return 0
    }
}
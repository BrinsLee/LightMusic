package com.brins.lightmusic.ui.customview

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.brins.lightmusic.R

class LoadingFragment : BaseDialogFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.loading_layout
    }

    companion object {
        @Synchronized
        fun showSelf(manager: FragmentManager): LoadingFragment {
            val logoutDialog = LoadingFragment()
            logoutDialog.show(manager)
            return logoutDialog
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        retainInstance = true
        super.onCreate(savedInstanceState)
    }

    override fun isCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun isInterceptKeyCodeBack(): Boolean {
        return true
    }

    override fun getDialogAnimResId(): Int {
        return R.style.CustomCenterDialogAnim
    }

    override fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

}
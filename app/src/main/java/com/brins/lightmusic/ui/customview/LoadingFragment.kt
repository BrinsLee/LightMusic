package com.brins.lightmusic.ui.customview

import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.brins.lightmusic.R

class LoadingFragment : BaseDialogFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.loading_layout
    }

    companion object {
        fun showSelf(manager: FragmentManager?): LoadingFragment? {
            if (manager == null){
                return  null
            }
            val logoutDialog = LoadingFragment()
            logoutDialog.show(manager)
            return logoutDialog
        }
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
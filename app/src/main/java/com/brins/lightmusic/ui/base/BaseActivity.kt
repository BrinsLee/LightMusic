package com.brins.lightmusic.ui.base

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.fragment.quickcontrol.QuickControlFragment
import com.jaeger.library.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.simpleName
    protected var fragment: QuickControlFragment = QuickControlFragment.newInstance()
    protected var firstTime : Long = 0
    protected var mBindDestroyDisposable: CompositeDisposable? = null

    protected open fun getOffsetView(): View? {
        return null
    }

    /*
*设置状态栏透明
* */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.gery, null)
        }else{
            window.statusBarColor = resources.getColor(R.color.gery)
        }
    }

    protected abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateBeforeBinding(savedInstanceState)
        var resId = getLayoutResId()
        if (resId != 0) {
            setContentView(resId)
        }
        StatusBarUtil.setTranslucentForImageView(this, 0, getOffsetView())
        onCreateAfterBinding(savedInstanceState)

    }

    protected open fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        bindUntilDestroy(subscribeEvents())
    }

    protected open fun onCreateBeforeBinding(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        if (mBindDestroyDisposable != null) {
            mBindDestroyDisposable!!.clear()
        }
    }

    protected open fun showBottomBar(fragmentManager: FragmentManager) {
        val ft = fragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.bottom_container, fragment).commit()
        }
    }

    protected open fun removeBottomBar(fragmentManager: FragmentManager) {
        val ft = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            ft.remove(fragment).commit()
        }
    }

    protected open fun getCurrentList(): PlayList? {
        return fragment.getCurrentList()
    }

    fun bindUntilDestroy(disposable: Disposable?) {
        if (disposable == null) {
            return
        }
        if (mBindDestroyDisposable == null) {
            mBindDestroyDisposable = CompositeDisposable()
        }
        mBindDestroyDisposable!!.add(disposable)
    }

    protected fun subscribeEvents(): Disposable? {
        return null
    }
}

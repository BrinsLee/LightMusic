package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.fragment.quickcontrol.QuickControlFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription


abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.simpleName
    protected var fragment : QuickControlFragment? = null
    protected var mBindDestroyDisposable: CompositeDisposable? = null
    protected val STATU_BAR = 20
/*
*设置状态栏透明
* */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = option
        window.statusBarColor = resources.getColor(R.color.alpha,null)
    }

    protected abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateBeforeBinding(savedInstanceState)
        var resId = getLayoutResId()
        if (resId != 0){
            setContentView(resId)
        }
        onCreateAfterBinding(savedInstanceState)

    }

    protected open fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        bindUntilDestroy(subscribeEvents())
    }

    protected open fun onCreateBeforeBinding(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        if (mBindDestroyDisposable != null){
            mBindDestroyDisposable!!.clear()
        }
    }

    protected open fun showBottomBar(show : Boolean){
        val ft = supportFragmentManager.beginTransaction()
        if (show) {
            if (fragment == null) {
                fragment = QuickControlFragment.newInstance()
                ft.add(R.id.bottom_container, fragment!!).commitAllowingStateLoss()
            } else {
                ft.show(fragment!!).commitAllowingStateLoss()
            }
        } else {
            if (fragment != null)
                ft.hide(fragment!!).commitAllowingStateLoss()
        }
    }

    fun bindUntilDestroy(disposable: Disposable?) {
        if (disposable == null){
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

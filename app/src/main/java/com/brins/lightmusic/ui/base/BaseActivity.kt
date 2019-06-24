package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.brins.lightmusic.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.simpleName
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
    }

    protected open fun onCreateBeforeBinding(savedInstanceState: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        if (mBindDestroyDisposable != null){
            mBindDestroyDisposable!!.clear()
        }
    }

    fun bindUntilDestroy(disposable: Disposable) {
        if (mBindDestroyDisposable == null) {
            mBindDestroyDisposable = CompositeDisposable()
        }
        mBindDestroyDisposable!!.add(disposable)
    }
}

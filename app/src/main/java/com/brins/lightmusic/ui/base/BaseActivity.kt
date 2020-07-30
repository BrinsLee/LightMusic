package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.brins.lightmusic.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected var TAG = this.javaClass.simpleName
    /*    @Inject
        protected lateinit var mPresenter: BasePresenter<BaseView>*/
    protected var firstTime: Long = 0
    protected var mBindDestroyDisposable: CompositeDisposable? = null
    protected open var currentFragment: Fragment? = null

    protected open fun getOffsetView(): View? {
        return null
    }

    override fun showLoading() {
    }

    override fun hideLoading() {

    }

    protected abstract fun getLayoutResId(): Int


    protected open fun isSubscribe(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateBeforeBinding(savedInstanceState)
        val resId = getLayoutResId()
        if (resId != 0) {
            setContentView(resId)
        }
        if (isSubscribe()){
            RxBus.getInstance().register(this)
        }
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
        if (isSubscribe()){
            RxBus.getInstance().unregister(this)
        }
    }


    private fun bindUntilDestroy(disposable: Disposable?) {
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

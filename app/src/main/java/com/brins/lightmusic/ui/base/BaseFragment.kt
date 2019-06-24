package com.brins.lightmusic.ui.base


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {

    val TAG = javaClass.simpleName
    /*实现懒加载*/
    protected var mIsViewBinding: Boolean = false
    protected var mIsVisibleToUser: Boolean = false
    protected var mHadLoaded: Boolean = false

    abstract fun getLayoutResID(): Int

    protected open fun onCreateViewAfterBinding(view : View){
        mIsViewBinding = true
        checkLoad()
    }

    private fun checkLoad() {
        if (!mHadLoaded && mIsViewBinding && mIsVisibleToUser) {
            onLazyLoad()
            mHadLoaded = true
        }
    }

    open fun onLazyLoad() {}

    protected open fun beforeCreateView() {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            checkLoad()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResID(), container, false)
    }

    fun bindUntilDestroy(disposable: Disposable) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).bindUntilDestroy(disposable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsViewBinding = false
        mIsVisibleToUser = false
        mHadLoaded = false
    }
}

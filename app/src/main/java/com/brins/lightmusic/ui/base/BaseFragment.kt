package com.brins.lightmusic.ui.base


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brins.lightmusic.utils.StarterCommon
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment(), BaseView {

    val TAG = javaClass.simpleName
    protected open var mBindDestroyDisposable: CompositeDisposable? = null
    private var rootView: View? = null
    /*实现懒加载*/
    protected open var mIsViewBinding: Boolean = false
    protected open var mIsVisibleToUser: Boolean = false
    protected open var mHadLoaded = false
    protected var mStarterCommon: StarterCommon? = null

    abstract fun getLayoutResID(): Int

    protected abstract fun initInject()


    protected open fun onCreateViewAfterBinding() {
        mIsViewBinding = true
        mStarterCommon = StarterCommon(activity)
        checkLoad()
    }


    private fun checkLoad() {
        if (!mHadLoaded && mIsViewBinding && mIsVisibleToUser) {
            onLazyLoad()
            bindUntilDestroy(subscribeEvents())
            mHadLoaded = true
        }
    }

    open fun subscribeEvents(): Disposable? {
        return null
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

    open fun onLazyLoad() {}

    protected open fun beforeCreateView() {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            checkLoad()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResID(), container, false)
        }
        initInject()
        return rootView
    }

    protected open fun showRetryView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateViewAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsViewBinding = false
        mIsVisibleToUser = false
        mHadLoaded = false
        if (mBindDestroyDisposable != null) {
            mBindDestroyDisposable!!.clear()
        }
        (rootView?.parent as ViewGroup).removeView(rootView)
    }


    override fun showLoading() {
        if (mStarterCommon != null) {
            mStarterCommon!!.showUnBackProgressLoading("")
        }
    }

    override fun hideLoading() {
        if (mStarterCommon != null) {
            mStarterCommon!!.dismissUnBackProgressLoading()
        }
    }


/*    override fun getcontext(): Context {
        return context!!
    }*/


    override fun handleError(error: Throwable) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mStarterCommon?.onDestroy()
        mStarterCommon = null
    }
}

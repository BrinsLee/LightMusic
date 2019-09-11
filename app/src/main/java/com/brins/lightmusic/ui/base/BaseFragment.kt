package com.brins.lightmusic.ui.base


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.ui.customview.LoadingFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T> : Fragment() , BaseView<T> {

    val TAG = javaClass.simpleName
    protected open var mBindDestroyDisposable: CompositeDisposable? = null
    private var rootView : View? = null
    /*实现懒加载*/
    protected open var mIsViewBinding: Boolean = false
    protected open var mIsVisibleToUser: Boolean = false
    protected open var mHadLoaded: Boolean = false
    protected open var loadingLayout: LoadingFragment? = null


    abstract fun getLayoutResID(): Int

    protected open fun onCreateViewAfterBinding(view : View){
        mIsViewBinding = true
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
        if (disposable == null){
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (rootView == null){
            rootView = inflater.inflate(getLayoutResID(), container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateViewAfterBinding(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mBindDestroyDisposable != null){
            mBindDestroyDisposable!!.clear()
        }
        (rootView?.parent as ViewGroup).removeView(rootView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsViewBinding = false
        mIsVisibleToUser = false
        mHadLoaded = false
    }

    override fun showLoading() {
        loadingLayout = LoadingFragment.showSelf(fragmentManager)
    }

    override fun hideLoading() {
        loadingLayout?.dismiss()
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }


/*    override fun getcontext(): Context {
        return context!!
    }*/


    override fun handleError(error: Throwable) {

    }
}

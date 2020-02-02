package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.di.component.ActivityComponent
import com.brins.lightmusic.di.component.DaggerActivityComponent
import com.brins.lightmusic.di.module.ActivityModule
import com.brins.lightmusic.ui.fragment.quickcontrol.QuickControlFragment
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

    override fun getLifeActivity(): AppCompatActivity {
        return this
    }

    protected abstract fun getLayoutResId(): Int

    protected abstract fun initInject()

    protected open fun isSubscribe(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateBeforeBinding(savedInstanceState)
        var resId = getLayoutResId()
        if (resId != 0) {
            setContentView(resId)
        }
        initInject()
        if (isSubscribe()){
            RxBus.getInstance().register(this)
        }
        onCreateAfterBinding(savedInstanceState)

    }

    protected open fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        bindUntilDestroy(subscribeEvents())
    }

    protected open fun onCreateBeforeBinding(savedInstanceState: Bundle?) {}

    protected fun getActivityComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
            .appComponent(LightMusicApplication.getAppComponent())
            .activityModule(getActivityModule())
            .build()
    }

    protected fun getActivityModule(): ActivityModule {
        return ActivityModule(this)
    }

    /*fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.add(
                R.id.drawer, targetFragment
                , targetFragment::class.java.name
            )
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.show(targetFragment)
        }
        currentFragment = targetFragment
        return transaction
    }

    fun switchFragment(targetFragment: Fragment, bundle: Bundle): FragmentTransaction {
        targetFragment.arguments = bundle
        return switchFragment(targetFragment)

    }*/


    override fun onDestroy() {
        super.onDestroy()
        if (mBindDestroyDisposable != null) {
            mBindDestroyDisposable!!.clear()
        }
        if (isSubscribe()){
            RxBus.getInstance().unregister(this)
        }
    }

/*    protected open fun showBottomBar(fragmentManager: FragmentManager) {
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
    }*/


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

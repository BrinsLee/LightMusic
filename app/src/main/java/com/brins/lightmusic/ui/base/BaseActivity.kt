package com.brins.lightmusic.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.fragment.quickcontrol.QuickControlFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.simpleName
    protected var fragment: QuickControlFragment = QuickControlFragment.newInstance()
    protected var firstTime : Long = 0
    protected var mBindDestroyDisposable: CompositeDisposable? = null
    protected open var currentFragment: Fragment? = null

    protected open fun getOffsetView(): View? {
        return null
    }

    /*
*设置状态栏透明
* */
/*    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.parseColor("#00323232")
        }
    }*/

    protected abstract fun getLayoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateBeforeBinding(savedInstanceState)
        var resId = getLayoutResId()


        if (resId != 0) {
            setContentView(resId)
        }
        onCreateAfterBinding(savedInstanceState)

    }

    protected open fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        bindUntilDestroy(subscribeEvents())
    }

    protected open fun onCreateBeforeBinding(savedInstanceState: Bundle?) {}

    fun switchFragment(targetFragment: Fragment): FragmentTransaction {
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

    }


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

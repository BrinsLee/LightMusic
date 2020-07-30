package com.brins.lightmusic

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.manager.AppManager
import com.brins.lightmusic.model.database.DatabaseFactory
import com.brins.lightmusic.utils.*
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        @JvmStatic
        var sInstance: BaseApplication? = null

        fun getInstance(): BaseApplication {
            if (sInstance == null) {
                throw NullPointerException("please inherit com.brins.lightmusic.BaseApplication or call setApplication.")
            }
            return sInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            //异常处理
        }
        setApplication(this)
        if (isMainProcess(this)) {
            initRxJava()
            initUserData()
        }
    }

    @Synchronized
    private fun setApplication(baseApplication: BaseApplication) {
        sInstance = baseApplication
        baseApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                AppManager.getAppManager().removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                AppManager.getAppManager().addActivity(activity)
            }

        })
    }


    fun isMainProcess(context: Context): Boolean {
        return AppConfig.Package.MAIN_PROCESS_NAME == getCurrProcessName(context)
    }


    private fun initRxJava() {
        RxJavaPlugins.setErrorHandler { Log.e("RxJava", "RX error handler") }
    }

    private fun initUserData() {
        AppConfig.isLogin = SpUtils.obtain(SP_USER_INFO, this).getBoolean(KEY_IS_LOGIN, false)
        AppConfig.UserCookie = SpUtils.obtain(SP_USER_INFO, this).getString(KEY_COOKIE, "")
        if (AppConfig.isLogin) {
            DatabaseFactory.getUserInfo().subscribeDbResult({
                AppConfig.userAccount = it
            }, {
                it.printStackTrace()
            })

            DatabaseFactory.getUserProfile().subscribeDbResult({
                AppConfig.userProfile = it
            }, {
                it.printStackTrace()
            })
        }
    }
}
package com.brins.lightmusic

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.brins.lightmusic.manager.AppManager
import com.brins.lightmusic.manager.TTAdManagerHolder

open class BaseApplication : Application(){

    companion object{
        @JvmStatic
        protected var sInstance: BaseApplication? = null

        fun getInstance(): BaseApplication {
            if (sInstance == null) {
                throw NullPointerException("please inherit com.brins.lightmusic.BaseApplication or call setApplication.")
            }
            return sInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        TTAdManagerHolder.init(this)
    }

    @Synchronized private fun setApplication(baseApplication: BaseApplication){
        sInstance = baseApplication
        baseApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
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

}
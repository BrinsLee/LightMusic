package com.brins.lightmusic

import android.content.Context
import android.util.Log
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.database.DatabaseFactory
import com.brins.lightmusic.utils.*
import io.reactivex.plugins.RxJavaPlugins


class LightMusicApplication : BaseApplication() {


    companion object {
        @JvmStatic
        fun getLightApplication(): LightMusicApplication {
            return sInstance as LightMusicApplication
        }
    }


    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            initRxJava()
            initUserData()
        }
    }

    fun isMainProcess(context: Context): Boolean {
        return AppConfig.Package.MAIN_PROCESS_NAME.equals(getCurrProcessName(context))
    }


    private fun initRxJava() {
        RxJavaPlugins.setErrorHandler { Log.e("RxJava", "RX error handler") }
    }

    private fun initUserData() {
        AppConfig.isLogin = SpUtils.obtain(SP_USER_INFO, this).getBoolean(KEY_IS_LOGIN, false)
        AppConfig.UserCookie = SpUtils.obtain(SP_USER_INFO, this).getString(KEY_COOKIE,"")
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
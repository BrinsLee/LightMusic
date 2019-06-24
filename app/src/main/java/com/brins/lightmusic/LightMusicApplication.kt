package com.brins.lightmusic

import android.content.Context
import android.util.Log
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.utils.MachineUtils
import io.reactivex.plugins.RxJavaPlugins

class LightMusicApplication : BaseApplication() {

    var isExitHome : Boolean = true

    companion object {
        fun  getLightApplication() : LightMusicApplication {
            return sInstance as LightMusicApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        if(isMainProcess(this)){
            initRxJava()
        }
    }

    fun isMainProcess(context: Context): Boolean {
        return AppConfig.Package.MAIN_PROCESS_NAME.equals(MachineUtils.getCurrProcessName(context))
    }


    private fun initRxJava() {
        RxJavaPlugins.setErrorHandler { throwable -> Log.e("RxJava", "RX error handler") }
    }
}
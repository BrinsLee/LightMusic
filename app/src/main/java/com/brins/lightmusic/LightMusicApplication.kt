package com.brins.lightmusic

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.database.DatabaseFactory
import com.brins.lightmusic.utils.*
import io.reactivex.plugins.RxJavaPlugins
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.file.FileNameGenerator
import java.util.zip.DataFormatException


class LightMusicApplication : BaseApplication() {

    var isExitHome: Boolean = true
    private var proxy: HttpProxyCacheServer? = null

    companion object {
        @JvmStatic
        fun getLightApplication(): LightMusicApplication {
            return sInstance as LightMusicApplication
        }
    }

    fun getProxy(context: Context): HttpProxyCacheServer {
        val myApplication = getLightApplication()
        if (myApplication.proxy == null)
            myApplication.proxy = myApplication.newProxy()
        return myApplication.proxy!!
    }

    fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this).cacheDirectory(getAudioCacheDir(this))
            .fileNameGenerator(NameGenerator()).build()
    }


    open class NameGenerator : FileNameGenerator {
        override fun generate(url: String): String {
            val uri = Uri.parse(url)
            val audioId = uri.getQueryParameter("guid")
            return audioId + "mp3"
        }

    }

    override fun onCreate() {
        super.onCreate()
        if (isMainProcess(this)) {
            initRxJava()
            initUserData()
        }
        /*if (LeakCanary.isInAnalyzerProcess(this)) {//1
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)*/
    }

    fun isMainProcess(context: Context): Boolean {
        return AppConfig.Package.MAIN_PROCESS_NAME.equals(getCurrProcessName(context))
    }


    private fun initRxJava() {
        RxJavaPlugins.setErrorHandler { throwable -> Log.e("RxJava", "RX error handler") }
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
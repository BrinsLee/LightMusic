package com.brins.lightmusic

import android.content.Context
import android.net.Uri
import android.util.Log
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.utils.getAudioCacheDir
import com.brins.lightmusic.utils.getCurrProcessName
import io.reactivex.plugins.RxJavaPlugins
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.file.FileNameGenerator


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
}
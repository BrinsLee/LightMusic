package com.brins.lightmusic.manager

import android.content.Context
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdManager
import com.bytedance.sdk.openadsdk.TTAdSdk

class TTAdManagerHolder {
    companion object{
        @JvmStatic var sInit = false

        @JvmStatic fun get() : TTAdManager{
            if (!sInit){
                throw RuntimeException("TTAdSdk is not init, please check.")
            }
            return TTAdSdk.getAdManager()
        }
        fun init(context: Context) {
            doInit(context)
        }

        private fun doInit(context: Context) {
            if (!sInit) {
                TTAdSdk.init(context, buildConfig(context))
                sInit = true
            }
        }

        private fun buildConfig(context: Context): TTAdConfig {
            return TTAdConfig.Builder()
                .appId("5001121")
                .useTextureView(true)
                .appName("FunClock")
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true)
                .allowShowPageWhenScreenLock(true)
                .debug(true)//Todo release包去掉这个
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_4G)
                .supportMultiProcess(false)
                .build()
        }
    }
}
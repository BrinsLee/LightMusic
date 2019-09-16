package com.brins.lightmusic.api

import android.util.Log
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.utils.KEY_COOKIE
import com.brins.lightmusic.utils.SP_USER_INFO
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.StringBuilder

class ReceivedCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val stringBuilder = StringBuilder()
        if (response.headers("Set-Cookie").isNotEmpty()) {
            response.headers("Set-Cookie").forEach {
                stringBuilder.append(it)
            }
            AppConfig.UserCookie = stringBuilder.toString()
            SpUtils.obtain(SP_USER_INFO, BaseApplication.getInstance().baseContext)
                .save(KEY_COOKIE, AppConfig.UserCookie)

            Log.d("InterceptorCookie:", AppConfig.UserCookie)
        }
        return response
    }


}
package com.brins.lightmusic.api.service

import android.text.TextUtils
import android.util.Log
import com.brins.lightmusic.common.AppConfig
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        Log.d("Cookie:", AppConfig.UserCookie)
        if (!TextUtils.isEmpty(AppConfig.UserCookie)) {
            builder.addHeader("Cookie", AppConfig.UserCookie)
        }
        return chain.proceed(builder.build())
    }
}
package com.brins.lightmusic.api

import com.brins.lightmusic.api.service.AddCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory {

    companion object {
        private val TIMEOUT_CONNECTED: Long = 15
        private val TIMEOUT_READ: Long = 15
        private val TIMEOUT_WRITE = 60

        fun newRetrofit(baseUrl: String): Retrofit {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build()
            return retrofit
        }

        private fun getClient(): OkHttpClient {
            val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
            builder.connectTimeout(TIMEOUT_CONNECTED, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(ReceivedCookiesInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return builder.build()
        }
    }


}
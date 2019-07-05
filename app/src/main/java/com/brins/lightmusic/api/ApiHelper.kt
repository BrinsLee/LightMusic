package com.brins.lightmusic.api

import com.brins.lightmusic.api.service.MusicService
import com.brins.lightmusic.common.AppConfig.BASEURL
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.model.PlayList
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {

        fun getRetrofitFactory(baseurl : String): MusicService {
            val retrofit = Retrofit.Builder().baseUrl(baseurl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build()
            return retrofit.create(MusicService::class.java)
        }

        fun  getClient() : OkHttpClient {
            val builder : OkHttpClient.Builder = OkHttpClient().newBuilder()
            val client = builder.connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
            return client
        }


    fun getArtist(i: Int): Observable<MutableList<Artist>> {
        return getRetrofitFactory(BASEURL).getArtist(i)
    }

    fun getPlayList(i : Int): Observable<MutableList<MusicList>>{
        return getRetrofitFactory(BASEURL).getPlayList(i)
    }
}
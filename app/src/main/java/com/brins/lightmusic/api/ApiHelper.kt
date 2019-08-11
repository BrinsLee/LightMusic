package com.brins.lightmusic.api

import com.brins.lightmusic.api.service.MusicService
import com.brins.lightmusic.common.AppConfig.BASEURL
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.model.onlinemusic.MusicBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import com.brins.lightmusic.model.onlinemusic.MusicMetaDataBean
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {

    private fun getRetrofitFactory(baseurl: String): MusicService {
        val retrofit = Retrofit.Builder().baseUrl(baseurl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
        return retrofit.create(MusicService::class.java)
    }

    fun getClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
        val client = builder.connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        return client
    }


    fun getArtist(i: Int): Observable<MusicListResult> {
        return getRetrofitFactory(BASEURL).getArtist(i)
    }

    fun getPlayList(i: Int): Observable<MusicListResult> {
        return getRetrofitFactory(BASEURL).getPlayList(i)
    }

    fun getPlayListDetail(id: String): Observable<MusicListDetailResult> {
        return getRetrofitFactory(BASEURL).getPlayListDetail(id)
    }

    fun getMusicDetail(ids: String): Observable<MusicMetaDataBean> {
        return getRetrofitFactory(BASEURL).getAlbum(ids)
    }

    fun getMusicUrl(ids: String): Observable<MusicBean> {
        return getRetrofitFactory(BASEURL).getUrl(ids)
    }

    fun getLatestMvData(limit: String = "10"): Observable<MvResult> {
        return getRetrofitFactory(BASEURL).getLatestMusicVideo(limit)
    }

    fun getMvMetaData(id: String): Observable<MvMetaResult> {
        return getRetrofitFactory(BASEURL).getMvMetaData(id)
    }
}
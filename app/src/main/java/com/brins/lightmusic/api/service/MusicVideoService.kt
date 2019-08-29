package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.model.musicvideo.MvResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicVideoService {


    @GET(AppConfig.LASTESTMUSICVIDEO)
    fun getLatestMusicVideo(@Query("limit") limit: Int): Observable<MvResult>

    @GET(AppConfig.MVURL)
    fun getMvMetaData(@Query("id") id: String): Observable<MvMetaResult>
}
package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayListService {

    /*
    * 获取歌单列表
    * */
    @GET(AppConfig.PLAYLIST)
    fun getPlayList(@Query("limit") limit: Int): Observable<MusicListResult>

    /*
    * 获取歌单详情信息
    * */
    @GET(AppConfig.PLAYLISTDETAIL)
    fun getPlayListDetail(@Query("id") id: String): Observable<MusicListDetailResult>
}
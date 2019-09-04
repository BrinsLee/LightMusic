package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.banner.BannerResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryService {

    /*
    * 加载轮播图
    * */
    @GET(AppConfig.BANNER)
    fun getBanner(@Query("type") type: Int = 1): Observable<BannerResult>

}
package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.dailyrecommend.DailyRecommendResult
import com.brins.lightmusic.model.userfm.UserFmResult
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserPlayListService {

    @GET(AppConfig.USER_PLAYLIST)
    fun getUserPlayList(@Query("uid")id : String): Observable<UserPlayListResult>

    @GET(AppConfig.USER_FM)
    fun getUserFm(): Observable<UserFmResult>

    @GET(AppConfig.DAILY_RECOMMEND)
    fun getDailyRecommend(): Call<DailyRecommendResult>
}
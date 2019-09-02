package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UserPlayListService {

    @GET(AppConfig.USER_PLAYLIST)
    fun getUserPlayList(@Query("uid")id : String): Observable<UserPlayListResult>
}
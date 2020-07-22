package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig.RECOMMEND.RECOMMEND_MUSIC_LIST
import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.model.personal.PersonalizedResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author lipeilin
 * @date 2020/7/22
 */
interface PersonalizedService {


    @GET(RECOMMEND_MUSIC_LIST)
    fun getPersonalizedMusic(@Query("limit") limit: Int = 10) : Call<PersonalizedResult<PersonalizedMusic>>

}
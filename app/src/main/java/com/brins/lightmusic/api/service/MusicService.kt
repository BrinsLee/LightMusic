package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig.*
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.PlayList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicService {

    @Headers("Accept: */*",
        "User-Agent: $USER_AGENT")
    /*
    * 登录网络接口
    * */
    /*@GET(LOGIN_EMAIL)
    fun Login_email(@Query("email")email : String,@Query("password")password :String): Observable<UserInfo>
*/

    /*
    * 获取歌手列表
    * */
    @GET(ARTISTS)
    fun getArtist(@Query("limit")limit:Int): Observable<MutableList<Artist>>

}
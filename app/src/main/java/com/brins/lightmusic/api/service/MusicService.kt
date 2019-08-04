package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig.*
import com.brins.lightmusic.model.*
import com.brins.lightmusic.model.musicvideo.LastestMvData
import com.brins.lightmusic.model.musicvideo.MvMetaData
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.model.musicvideo.MvResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MusicService {

    @Headers(
        "Accept: */*",
        "User-Agent: $USER_AGENT"
    )
    /*
    * 登录网络接口
    * */
    /*@GET(LOGIN_EMAIL)
    fun Login_email(@Query("email")email : String,@Query("password")password :String): Observable<UserInfo>
*/

    /*
    * 获取歌单列表
    * */
    @GET(PLAYLIST)
    fun getPlayList(@Query("limit") limit: Int): Observable<Data>

    /*
    * 获取歌单详情信息
    * */
    @GET(PLAYLISTDETAIL)
    fun getPlayListDetail(@Query("id") id: String): Observable<MusicListDetail>

    /*
    * 获取歌手列表
    * */
    @GET(ARTISTS)
    fun getArtist(@Query("limit") limit: Int): Observable<Data>

    @GET(ALBUM)
    fun getAlbum(@Query("ids") ids: String): Observable<MusicMetaData>

    @GET(SONG)
    fun getUrl(@Query("id") id: String): Observable<Songs>

    @GET(LASTESTMUSICVIDEO)
    fun getLatestMusicVideo(@Query("limit") limit: String = "10"): Observable<MvResult>

    @GET(MVURL)
    fun getMvMetaData(@Query("id") id: String): Observable<MvMetaResult>
}
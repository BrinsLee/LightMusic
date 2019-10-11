package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.album.AlbumListResult
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.artist.CategoryResultData
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistService {


    /*
   * 获取歌手列表
   * */
    @GET(AppConfig.ARTISTS)
    fun getArtist(@Query("limit") limit: Int = 15): Call<MusicListResult>


    /*
  * 获取歌手分类列表
  * */
    @GET(AppConfig.ARTISTS_CATEGORY)
    fun getArtistCategory(@Query("cat") cat: Int = 1001): Call<CategoryResultData>
    /*
    * 获取歌手音乐列表
    * */
    @GET(AppConfig.ARTISTS_MUSIC)
    fun getArtistMusic(@Query("id") id: String): Call<ArtistSongResult>

    /*
    *  获取歌手MV列表
    * */
    @GET(AppConfig.ARTISTS_MV)
    fun getArtistMV(@Query("id") id: String, @Query("limit") limit: Int ): Call<MvResult>

    /*
    * 获取歌手专辑
    * */
    @GET(AppConfig.ARTIST_ALBUM)
    fun getArtistAlbum(@Query("id") id:String): Call<AlbumListResult>
}
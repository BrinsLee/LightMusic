package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig.*
import com.brins.lightmusic.model.artist.ArtistResult
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.model.onlinemusic.MusicBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import com.brins.lightmusic.model.onlinemusic.MusicMetaDataBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MusicService {

    @Headers(
        "Accept: */*",
        "User-Agent: $USER_AGENT"
    )
    @GET(ALBUM)
    fun getAlbum(@Query("ids") ids: String): Observable<MusicMetaDataBean>

    @GET(SONG)
    fun getUrl(@Query("id") id: String): Observable<MusicBean>



}
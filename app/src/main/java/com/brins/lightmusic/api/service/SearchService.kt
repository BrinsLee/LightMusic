package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.search.SearchResult
import com.brins.lightmusic.model.search.SearchSuggestResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    /**
     * 音乐搜索结果
     */
    @GET(AppConfig.SEARCH)
    fun searchMusic(@Query("keywords")keys : String, @Query("type")type: Int = 1): Call<SearchResult<Music>>

    /**
     * 专辑搜索结果
     */
    @GET(AppConfig.SEARCH)
    fun searchAlbum(@Query("keywords")keys : String, @Query("type")type: Int = 10): Call<SearchResult<Album>>

    /**
     * 歌手搜索结果
     */
    @GET(AppConfig.SEARCH)
    fun searchArtist(@Query("keywords")keys : String, @Query("type")type: Int = 100): Call<SearchResult<ArtistBean>>


    /*
    *搜索建议
    */
    @GET(AppConfig.SEARCH_SUGGEST)
    fun searchSuggest(@Query("keywords")keys : String, @Query("type")type: String = "mobile"): Call<SearchSuggestResult>
}
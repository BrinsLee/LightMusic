package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.search.SearchSuggestResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    /**
     * 搜索结果
     */
    @GET(AppConfig.SEARCH)
    fun <T>search(@Query("keywords")keys : String,@Query("type")type: Int = 1): Call<T>

    /*
    *搜索建议
    */
    @GET(AppConfig.SEARCH_SUGGEST)
    fun searchSuggest(@Query("keywords")keys : String, @Query("type")type: String = "mobile"): Call<SearchSuggestResult>
}
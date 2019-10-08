package com.brins.lightmusic.model.search

import com.brins.lightmusic.model.Music
import com.google.gson.annotations.SerializedName

class SearchResult<T> {

    @SerializedName("result")
    var dataBean: SearchResultBean<T>? = null

}
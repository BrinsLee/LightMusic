package com.brins.lightmusic.model.search

import com.google.gson.annotations.SerializedName

class SearchResultBean<T> {

    @SerializedName("songs")
    var songs : ArrayList<T>? = null
}

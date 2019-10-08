package com.brins.lightmusic.model.search

import com.google.gson.annotations.SerializedName

class SearchResultBean<T> {

    @SerializedName("songs",alternate = ["albums","artists","playlists","mvs"])
    var data : ArrayList<T>? = null
}

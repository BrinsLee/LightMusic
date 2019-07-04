package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("playlists")
    var playlists : List<PlayList>? = null
    @SerializedName("artists")
    var artists : List<Artist>? = null
}
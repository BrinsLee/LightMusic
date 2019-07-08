package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("playlists")
    var playlists : List<MusicList>? = null
    @SerializedName("artists")
    var artists : List<Artist>? = null
}
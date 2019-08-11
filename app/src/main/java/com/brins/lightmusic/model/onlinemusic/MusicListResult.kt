package com.brins.lightmusic.model.onlinemusic

import com.brins.lightmusic.model.artist.ArtistBean
import com.google.gson.annotations.SerializedName

class MusicListResult {
    @SerializedName("playlists")
    var playlists : List<MusicListBean>? = null
    @SerializedName("artists")
    var artistBeans : List<ArtistBean>? = null
}
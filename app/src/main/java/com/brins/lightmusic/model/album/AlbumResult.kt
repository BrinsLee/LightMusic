package com.brins.lightmusic.model.album

import com.brins.lightmusic.model.Music
import com.google.gson.annotations.SerializedName

class AlbumResult {
    @SerializedName("songs")
    var songs : ArrayList<Music>? = null
    @SerializedName("album")
    var album: AlbumBean? = null
}
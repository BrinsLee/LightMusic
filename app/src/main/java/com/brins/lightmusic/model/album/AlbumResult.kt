package com.brins.lightmusic.model.album

import com.google.gson.annotations.SerializedName

class AlbumResult {
    @SerializedName("hotAlbums")
    var hotAlbums : ArrayList<AlbumBean>? = null
}
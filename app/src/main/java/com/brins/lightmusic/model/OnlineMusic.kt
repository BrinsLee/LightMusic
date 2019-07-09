package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class OnlineMusic() /*: Music(fileNmae, name, ar[0].name, al.name, al.picUrl, "", dt) */{

    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("ar")
    var artists: List<Artist>? = null
    @SerializedName("al")
    var album: Album? = null
    @SerializedName("dt")
    var duration : Int = 0
}
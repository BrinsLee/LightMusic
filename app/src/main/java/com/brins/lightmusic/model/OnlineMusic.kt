package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class OnlineMusic(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var nameMusic: String = "",
    @SerializedName("ar")
    var artists: List<Artist>? = null,
    @SerializedName("al")
    var al: Album? = null,
    @SerializedName("dt")
    var dt: Int = 0,

    var fileUrl: String = ""
) /*: Music(nameMusic, nameMusic, artists!![0].name, al!!.name, al.picUrl, fileUrl, dt) */{

}
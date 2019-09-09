package com.brins.lightmusic.model.onlinemusic

import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean
import com.google.gson.annotations.SerializedName

class OnlineMusic(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var nameMusic: String = "",
    @SerializedName("ar")
    var artistBeans: List<ArtistBean>? = null,
    @SerializedName("al")
    var al: Album? = null,
    @SerializedName("dt")
    var dt: Int = 0,

    var fileUrl: String = ""
)/* : Music(nameMusic, nameMusic, artistBeans!![0].name, al!!.name, al.picUrl, fileUrl, dt)*/ {

}
package com.brins.lightmusic.model.album

import com.brins.lightmusic.model.artist.ArtistBean
import com.google.gson.annotations.SerializedName

class AlbumBean {
    @SerializedName("artist")
    var artist: ArtistBean? = null

    @SerializedName("picUrl")
    var picUrl : String = ""

    @SerializedName("name")
    var name : String = ""

    @SerializedName("company")
    var company : String =""

    @SerializedName("subType")
    var subTYpe : String = ""

    @SerializedName("id")
    var id : String = ""

}
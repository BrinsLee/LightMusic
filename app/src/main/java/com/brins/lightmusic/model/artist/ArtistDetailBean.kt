package com.brins.lightmusic.model.artist

import com.google.gson.annotations.SerializedName

class ArtistDetailBean {

    @SerializedName("albumSize")
    var albumSize :Int = 0

    @SerializedName("alias")
    var alias : Alias? = null

    @SerializedName("briefDesc")
    var briefDesc : String = ""

    @SerializedName("img1v1Url")
    var avatar : String = ""

    companion object{
        class Alias {
            var alias : String = ""
        }
    }
}
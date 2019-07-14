package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class Songs {
    @SerializedName("data")
    var data : List<MusicMetaData>? = null
}
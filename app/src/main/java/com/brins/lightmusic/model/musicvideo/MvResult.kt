package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class MvResult {
    @SerializedName("data")
    var dataBeans : List<LastestMvDataBean>? = null
}
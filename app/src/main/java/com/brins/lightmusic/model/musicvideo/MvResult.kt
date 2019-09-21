package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class MvResult {
    @SerializedName("data",alternate = ["mvs"])
    var dataBeans : List<LastestMvDataBean>? = null
}
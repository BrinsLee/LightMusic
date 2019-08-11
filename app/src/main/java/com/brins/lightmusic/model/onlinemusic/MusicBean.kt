package com.brins.lightmusic.model.onlinemusic

import com.google.gson.annotations.SerializedName

class MusicBean {
    @SerializedName("data")
    var data : List<MusicMetaDataBean>? = null
}
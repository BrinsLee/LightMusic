package com.brins.lightmusic.model.banner

import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.google.gson.annotations.SerializedName

class Banner {

    @SerializedName("pic")
    var picUrl : String = ""

    @SerializedName("targetId")
    var targetId : String =""

    @SerializedName("titleColor")
    var titleColor : String = "white"

    @SerializedName("song")
    var song : OnlineMusic? = null

    @SerializedName("typeTitle")
    var typeTitle : String = ""

}

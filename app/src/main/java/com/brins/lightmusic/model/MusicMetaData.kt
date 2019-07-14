package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

/*
* 歌单中的歌曲信息
* */
class MusicMetaData {

    @SerializedName("type")
    var type : String = ""
    @SerializedName("payed")
    var payed : Int = 0
    @SerializedName("url")
    var url : String = ""
}
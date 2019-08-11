package com.brins.lightmusic.model.onlinemusic

import com.google.gson.annotations.SerializedName

/*
* 歌单中的歌曲信息
* */
class MusicMetaDataBean {

    @SerializedName("type")
    var type : String = ""
    @SerializedName("payed")
    var payed : Int = 0
    @SerializedName("url")
    var url : String = ""
}
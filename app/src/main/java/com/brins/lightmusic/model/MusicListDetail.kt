package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

/*
* 歌单详情
* */
class MusicListDetail {

    @SerializedName("playlist")
    var playlist : PlayListDetail? = null

}
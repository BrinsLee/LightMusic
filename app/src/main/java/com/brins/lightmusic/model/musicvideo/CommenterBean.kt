package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class CommenterBean {
    @SerializedName("nickname")
    var nickname: String = ""

    @SerializedName("avatarUrl")
    var avatarUrl: String = ""

    @SerializedName("userId")
    var userId: String = ""
}
package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class MvCommentsBean {
    @SerializedName("user")
    var user : CommenterBean? = null

    @SerializedName("commentId")
    var commentId : String = ""

    @SerializedName("content")
    var content : String = ""


}
package com.brins.lightmusic.model.userfm

import com.brins.lightmusic.model.Music
import com.google.gson.annotations.SerializedName

class UserFmResult {
    @SerializedName("data")
    var fmList : ArrayList<Music>? = null
}
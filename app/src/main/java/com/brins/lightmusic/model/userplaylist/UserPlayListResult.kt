package com.brins.lightmusic.model.userplaylist

import com.google.gson.annotations.SerializedName

class UserPlayListResult {

    @SerializedName("playlist")
    var playlist : ArrayList<UserPlayListBean>? = null

}
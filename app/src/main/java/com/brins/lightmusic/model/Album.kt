package com.brins.lightmusic.model

import com.google.gson.annotations.SerializedName

class Album {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("picUrl")
    var picUrl: String = ""
    @SerializedName("tns")
    var translate : Array<String>? = null
}
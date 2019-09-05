package com.brins.lightmusic.model.artist

import com.google.gson.annotations.SerializedName

class Category {

    @SerializedName("name")
    var name : String = ""

    @SerializedName("url")
    var url : String = ""

    @SerializedName("code")
    var code : Int = 0
}
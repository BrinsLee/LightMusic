package com.brins.lightmusic.model.search

import com.google.gson.annotations.SerializedName

class AllMatch {

    @SerializedName("keyword")
    var keyword : String = ""

    @SerializedName("type")
    var type : Int = 0
}
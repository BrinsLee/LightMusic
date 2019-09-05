package com.brins.lightmusic.model.artist

import com.google.gson.annotations.SerializedName

class CategoryResult {

    @SerializedName("artist")
    var artists : ArrayList<Category>? = null
}
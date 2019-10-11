package com.brins.lightmusic.model.artist

import com.google.gson.annotations.SerializedName

class CategoryResultData {

    @SerializedName("artists")
    var artists : ArrayList<ArtistBean>? = null
}
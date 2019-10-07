package com.brins.lightmusic.model.search

import com.brins.lightmusic.model.artist.ArtistBean
import com.google.gson.annotations.SerializedName

class SearchSuggestResult {

    @SerializedName("result")
    var result : SearchSuggest? = null
}
package com.brins.lightmusic.model.artist

import com.google.gson.annotations.SerializedName

class ArtistResult {
    @SerializedName("artist")
    var artist : ArtistDetailBean? = null

    @SerializedName("hotSongs")
    var hotSong : HotSongBean? = null
}
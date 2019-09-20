package com.brins.lightmusic.model.artist

import com.brins.lightmusic.model.Music
import com.google.gson.annotations.SerializedName

class ArtistSongResult {
    @SerializedName("hotSongs")
    var artist : ArrayList<Music>? = null
}
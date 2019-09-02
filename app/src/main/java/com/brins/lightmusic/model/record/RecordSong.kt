package com.brins.lightmusic.model.record

import com.google.gson.annotations.SerializedName

class RecordSong {
    @SerializedName("name")
    var name : String = ""
    @SerializedName("id")
    var SongId : String =""
    @SerializedName("ar")
    var artist : List<RecordArtist>? = null
    @SerializedName("al")
    var album : RecordAlbum = RecordAlbum()
}
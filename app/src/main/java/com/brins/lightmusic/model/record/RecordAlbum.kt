package com.brins.lightmusic.model.record

import com.google.gson.annotations.SerializedName

class RecordAlbum {

    @SerializedName("id")
    var AlbumId : String = ""
    @SerializedName("name")
    var AlbumName : String = ""
    @SerializedName("picUrl")
    var picUrl : String = ""


}
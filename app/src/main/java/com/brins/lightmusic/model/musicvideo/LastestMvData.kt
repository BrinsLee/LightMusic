package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class LastestMvData {

    @SerializedName("id")
    var id : String =  ""
    var cover : String = ""
    var name: String = ""
    var playCount : Int = 0
    var artistName: String = ""
    var duration: Int = 0
    var briefDesc : String = ""
    var sub : Boolean = false


}
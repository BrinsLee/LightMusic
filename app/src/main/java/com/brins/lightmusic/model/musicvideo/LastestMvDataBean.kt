package com.brins.lightmusic.model.musicvideo

import com.google.gson.annotations.SerializedName

class LastestMvDataBean {

    @SerializedName("id")
    var id : String =  ""
    var cover : String = ""
    var name: String = ""
    var playCount : Int = 0
    var artistId : String = ""
    var artistName: String = ""
    var duration: Int = 0
    var briefDesc : String = ""
    var sub : Boolean = false

    var subCount : Int = 0
    var shareCount : Int = 0
    var likeCount: Int = 0
    var commentCount : Int = 0

}
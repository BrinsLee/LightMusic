package com.brins.lightmusic.model

class OnlineMusic(
    var id: String = ""
    , fileNmae: String = ""
    , name: String = ""
    , ar: artist
    , al: Album
    , dt : Int
) : Music(fileNmae, name, ar.name, al.name, al.picUrl, "", dt) {

    inner class artist {
        var id: String = ""
        var name: String = ""
    }

    inner class Album {
        var id: String = ""
        var name: String = ""
        var picUrl: String = ""

    }
}
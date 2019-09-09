package com.brins.lightmusic.model.loaclmusic

import android.graphics.Bitmap
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean

class LocalMusic(
    id: Int = 0
    , fileNmae: String = ""
    , title: String = ""
    , artistBeans: List<ArtistBean>
    , var size: String
    , fileUrl: String
    , album: Album
    , var type: String = ""
    , duration: Int = 0
) : Music(fileNmae, title, artistBeans, album, duration, fileNmae, fileUrl) {
    var coverBitmap: Bitmap? = null
}
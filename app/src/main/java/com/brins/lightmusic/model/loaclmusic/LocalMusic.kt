package com.brins.lightmusic.model.loaclmusic

import android.graphics.Bitmap
import com.brins.lightmusic.model.Music

class LocalMusic(
    var id: Int = 0
    , fileNmae: String = ""
    , title: String = ""
    , singer: String = ""
    , var size: String = ""
    , fileUrl: String = ""
    , album: String = ""
    , var type: String = ""
    , bitmapcover: String? = ""
    , duration: Int = 0
) : Music(fileNmae, title, singer, album, bitmapcover, fileUrl , duration){
    var coverBitmap : Bitmap? = null
}
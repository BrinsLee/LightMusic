package com.brins.lightmusic.model

import android.graphics.Bitmap

class LocalMusic(
    var id: Int = 0, fileNmae: String = ""
    , title: String = ""
    , singer: String = ""
    , var size: String = ""
    , fileUrl: String = ""
    , album: String = ""
    , var type: String = ""
    , bitmapcover: String? = ""
    , duration: Int = 0
) : Music(fileNmae, title, singer, album, bitmapcover, fileUrl , duration)
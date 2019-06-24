package com.brins.lightmusic.model

import android.graphics.Bitmap

class LocalMusic (
        var id : Int = 0, fileNmae : String= ""
                  ,title : String=""
                  ,singer : String= ""
                  ,var duration : Int = 0
                  ,var size : String= ""
                  ,var fileUrl : String= ""
                  ,album : String= ""
                  ,var type : String = ""
                  ,bitmapcover : Bitmap? = null): Music(fileNmae,title,singer,album,bitmapcover) {

}
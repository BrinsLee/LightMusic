package com.brins.lightmusic.model.record

import com.google.gson.annotations.SerializedName

class RecordWeekData {

    @SerializedName("song")
    var song : RecordSong = RecordSong()
}
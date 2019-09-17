package com.brins.lightmusic.model.dailyrecommend

import com.brins.lightmusic.model.Music
import com.google.gson.annotations.SerializedName

class DailyRecommendResult {
    @SerializedName("recommend")
    var recommend : ArrayList<Music>? = null
}
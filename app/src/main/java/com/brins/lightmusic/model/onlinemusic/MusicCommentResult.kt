package com.brins.lightmusic.model.onlinemusic

import com.google.gson.annotations.SerializedName

class MusicCommentResult {

    @SerializedName("hotComments")
    var hotComments : List<HotComments>? = null
    companion object{
        class HotComments {
            @SerializedName("user")
            var user : User? = null

            @SerializedName("commentId")
            var commentId : String = ""

            @SerializedName("content")
            var content : String = ""

            var time : Long = 0

            var likedCount : Long = 0

            var liked : Boolean = false

        }

        class User {
            @SerializedName("userId")
            var userId : String = ""

            @SerializedName("avatarUrl")
            var avatarUrl : String = ""

            var nickname : String = ""

        }
    }
}
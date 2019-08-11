package com.brins.lightmusic.model.onlinemusic

/*
* 歌单创建详情，如创建者，创建时间，关注数，播放数
 */
class MusicListDetailBean {
    var id :String = ""
    var coverImgUrl : String = ""
    var createTime : Long = 0
    var updateTime : Long = 0
    var description : String = ""
    var name : String = ""
    var playCount : Int = 0
    var shareCount : Int = 0
    var subscribed : Boolean = false
    var subscribedCount : Int = 0
    var tracks : List<OnlineMusic>? = null
}
package com.brins.lightmusic.model.userlogin

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
class UserProfileBean {
    /**
     * 数据库ID
     * */
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var mId: Long = 0
    /**
     * 用户ID
     * */
    @ColumnInfo(name = "userId")
    var userId = 0
    /**
     * 背景图片
     * */
    @ColumnInfo(name = "background")
    var backgroundUrl: String = ""
    /**
     * 昵称
     * */
    @ColumnInfo(name = "nickname")
    var nickname: String = ""
    /**
     * 所在城市
     * */
    @ColumnInfo(name = "city")
    var city: Long = 0
    /**
     * 所在省份
     * */
    @ColumnInfo(name = "province")
    var province: Long = 0

    /**
     * 播放列表数量
     * */
    @ColumnInfo(name = "playlistCount")
    var playlistCount: Int = 0

    /**
     * 被订阅数
     * */
    @ColumnInfo(name = "playlistBeSubscribedCount")
    var playlistBeSubscribedCount: Int = 0

    /**
     * 粉丝数
     * */
    @ColumnInfo(name = "followeds")
    var followeds = 0

    /**
     * 关注人数
     * */
    @ColumnInfo(name = "follows")
    var follows = 0

    @ColumnInfo(name = "eventCount")
    var eventCount = 0
    /**
     * 默认头像
     * */
    @ColumnInfo(name = "defaultAvatar")
    var defaultAvatar = false
    /**
     * 头像连接
     * */
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl = ""

    /**
     * 个性签名
     * */
    @ColumnInfo(name = "signature")
    var signature = ""

    /**
     * 等级
     * */
    @ColumnInfo(name = "level")
    var level = 0

    /**
     * 性别
     * */
    @ColumnInfo(name = "gender")
    var gender = 0

}
package com.brins.lightmusic.model.userlogin

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_account")
class UserAccountBean {
    /**
     * 数据库用户ID
     * */
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var mId: Int = 0
    /**
     * 用户ID
     * */
    @ColumnInfo(name = "userId")
    @SerializedName("id")
    var id: String = ""

    /**
     * 用户名
     * */
    @ColumnInfo(name = "userName")
    var userName: String = ""

    /**
     * vip类型
     * */
    @ColumnInfo(name = "vipType")
    var vipType: Int = 0

    /**
     * vip版本
     * */
    @ColumnInfo(name = "vipTypeVersion")
    var viptypeVersion: Int = 0
}
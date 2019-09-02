package com.brins.lightmusic.model.userplaylist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserPlayListBean() : Parcelable {
    @SerializedName("creator")
    var creator : CreatorBean = CreatorBean()

    @SerializedName("subscribedCount")
    var subscribedCount = 0

    @SerializedName("coverImgUrl")
    var coverImgUrl = ""

    @SerializedName("createTime")
    var createTime : String = ""

    @SerializedName("playCount")
    var playCount = 0

    @SerializedName("id")
    var id : String = ""

    @SerializedName("trackCount")
    var trackCount = 0

    @SerializedName("userId")
    var userId : String =""

    @SerializedName("name")
    var name : String = ""

    constructor(parcel: Parcel) : this() {
        creator = parcel.readParcelable(CreatorBean::class.java.classLoader)
        subscribedCount = parcel.readInt()
        coverImgUrl = parcel.readString()
        createTime = parcel.readString()
        playCount = parcel.readInt()
        id = parcel.readString()
        trackCount = parcel.readInt()
        userId = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(creator, flags)
        parcel.writeInt(subscribedCount)
        parcel.writeString(coverImgUrl)
        parcel.writeString(createTime)
        parcel.writeInt(playCount)
        parcel.writeString(id)
        parcel.writeInt(trackCount)
        parcel.writeString(userId)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserPlayListBean> {
        override fun createFromParcel(parcel: Parcel): UserPlayListBean {
            return UserPlayListBean(parcel)
        }

        override fun newArray(size: Int): Array<UserPlayListBean?> {
            return arrayOfNulls(size)
        }
    }

}
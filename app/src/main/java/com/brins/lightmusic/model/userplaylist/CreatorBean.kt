package com.brins.lightmusic.model.userplaylist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CreatorBean() : Parcelable {
    @SerializedName("avatarUrl")
    var avatarUrl: String = ""
    @SerializedName("nickname")
    var nickName: String = ""
    @SerializedName("backgroundUrl")
    var backgroundUrl: String = ""

    constructor(parcel: Parcel) : this() {
        avatarUrl = parcel.readString()
        nickName = parcel.readString()
        backgroundUrl = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatarUrl)
        parcel.writeString(nickName)
        parcel.writeString(backgroundUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreatorBean> {
        override fun createFromParcel(parcel: Parcel): CreatorBean {
            return CreatorBean(parcel)
        }

        override fun newArray(size: Int): Array<CreatorBean?> {
            return arrayOfNulls(size)
        }
    }
}
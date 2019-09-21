package com.brins.lightmusic.model.musicvideo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class LastestMvDataBean() : Parcelable {

    @SerializedName("id")
    var id : String =  ""
    @SerializedName("cover",alternate = ["imgurl"])
    var cover : String = ""
    var name: String = ""
    var playCount : Int = 0
    var artistId : String = ""
    var artistName: String = ""
    var duration: Int = 0
    var briefDesc : String = ""
    var sub : Boolean = false

    var subCount : Int = 0
    var shareCount : Int = 0
    var likeCount: Int = 0
    var commentCount : Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        cover = parcel.readString()
        name = parcel.readString()
        playCount = parcel.readInt()
        artistId = parcel.readString()
        artistName = parcel.readString()
        duration = parcel.readInt()
        briefDesc = parcel.readString()
        sub = parcel.readByte() != 0.toByte()
        subCount = parcel.readInt()
        shareCount = parcel.readInt()
        likeCount = parcel.readInt()
        commentCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(cover)
        parcel.writeString(name)
        parcel.writeInt(playCount)
        parcel.writeString(artistId)
        parcel.writeString(artistName)
        parcel.writeInt(duration)
        parcel.writeString(briefDesc)
        parcel.writeByte(if (sub) 1 else 0)
        parcel.writeInt(subCount)
        parcel.writeInt(shareCount)
        parcel.writeInt(likeCount)
        parcel.writeInt(commentCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LastestMvDataBean> {
        override fun createFromParcel(parcel: Parcel): LastestMvDataBean {
            return LastestMvDataBean(parcel)
        }

        override fun newArray(size: Int): Array<LastestMvDataBean?> {
            return arrayOfNulls(size)
        }
    }

}
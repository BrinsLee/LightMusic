package com.brins.lightmusic.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

open class Music(
    var fileName: String, @Transient var name: String, var singer: String, var album: String, var cover: String?, var fileUrl: String = ""
    , var duration: Int
) : Parcelable {
    var bitmapCover : Bitmap? = null
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(name)
        parcel.writeString(singer)
        parcel.writeString(album)
        parcel.writeString(cover)
        parcel.writeString(fileUrl)
        parcel.writeInt(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}

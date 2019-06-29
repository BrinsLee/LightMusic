package com.brins.lightmusic.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

open class Music(var fileName : String,var title: String, var singer: String, var album: String,var cover : String? , var fileUrl : String = "") :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(title)
        parcel.writeString(singer)
        parcel.writeString(album)
        parcel.writeString(cover)
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

package com.brins.lightmusic.model.musicvideo

import android.os.Parcel
import android.os.Parcelable

class MvMetaDataBean() : Parcelable {

    var id : String = ""
    var url : String = ""
    var size: Long = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        url = parcel.readString()
        size = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(url)
        parcel.writeLong(size)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MvMetaDataBean> {
        override fun createFromParcel(parcel: Parcel): MvMetaDataBean {
            return MvMetaDataBean(parcel)
        }

        override fun newArray(size: Int): Array<MvMetaDataBean?> {
            return arrayOfNulls(size)
        }
    }
}
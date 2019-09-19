package com.brins.lightmusic.model.musicvideo

import android.os.Parcel
import android.os.Parcelable

class Mv(
    var dataBean: LastestMvDataBean,
    var metaDataBean: MvMetaDataBean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(LastestMvDataBean::class.java.classLoader),
        parcel.readParcelable(MvMetaDataBean::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(dataBean, flags)
        parcel.writeParcelable(metaDataBean, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mv> {
        override fun createFromParcel(parcel: Parcel): Mv {
            return Mv(parcel)
        }

        override fun newArray(size: Int): Array<Mv?> {
            return arrayOfNulls(size)
        }
    }
}
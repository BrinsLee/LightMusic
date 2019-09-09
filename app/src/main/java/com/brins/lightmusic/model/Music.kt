package com.brins.lightmusic.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean
import com.google.gson.annotations.SerializedName

open class Music(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String,
    @SerializedName("ar")
    var artistBeans: List<ArtistBean>? = null,
    @SerializedName("al")
    var album: Album,
    @SerializedName("dt")
    var duration: Int,

    var fileName: String = "",

    var fileUrl: String = ""

) : Parcelable {
    var bitmapCover : Bitmap? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(ArtistBean.CREATOR),
        parcel.readParcelable(Album::class.java.classLoader),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
        bitmapCover = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeTypedList(artistBeans)
        parcel.writeParcelable(album, flags)
        parcel.writeInt(duration)
        parcel.writeString(fileName)
        parcel.writeString(fileUrl)
        parcel.writeParcelable(bitmapCover, flags)
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

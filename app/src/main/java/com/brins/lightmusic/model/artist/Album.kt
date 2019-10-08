package com.brins.lightmusic.model.artist

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Album() : Parcelable {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("picUrl")
    var picUrl: String = ""
    @SerializedName("tns")
    var translate : Array<String>? = null
    @SerializedName("artist")
    var artist: ArtistBean? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        picUrl = parcel.readString()
        translate = parcel.createStringArray()
        artist = parcel.readParcelable(ArtistBean::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(picUrl)
        parcel.writeStringArray(translate)
        parcel.writeParcelable(artist, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }

}
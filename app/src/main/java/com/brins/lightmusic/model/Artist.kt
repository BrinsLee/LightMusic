package com.brins.lightmusic.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject


class Artist : Parcelable {
    var albumSize: Int = 0

    var picUrl: String = ""

    var name: String = ""

    var musicSize: Int = 0

    var id: String = ""

    var accountId: String = ""

    var bitmap: String = ""

    constructor(source: Parcel){
        albumSize = source.readInt()
        picUrl = source.readString()
        name = source.readString()
        musicSize = source.readInt()
        id = source.readString()
        accountId = source.readString()
        bitmap = source.readString()
    }

    constructor(jsonObject : JSONObject){
        this.name = jsonObject.optString("name")
        this.picUrl = jsonObject.optString("picUrl")
    }
    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        this.writeInt(albumSize)
        this.writeString(picUrl)
        this.writeString(name)
        this.writeInt(musicSize)
        this.writeString(id)
        this.writeString(accountId)
        this.writeString(bitmap)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Artist> = object : Parcelable.Creator<Artist> {
            override fun createFromParcel(source: Parcel): Artist = Artist(source)
            override fun newArray(size: Int): Array<Artist?> = arrayOfNulls(size)
        }
    }
}
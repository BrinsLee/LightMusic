

package com.brins.lightmusic.model

import android.os.Parcel
import android.os.Parcelable
/*
* 歌单
 */
class MusicList : Parcelable {
    var name: String = ""

    var id: String = ""

    var userid: String = ""

    var createTime: Long = 0

    var subscribedCount: Long = 0

    var coverImgUrl: String = ""

    var description: String = ""

    var tags = arrayOf("")

    var playCount: Long = 0

    var shareCount: Int = 0

    var commentCount: Int = 0

    var bitmap: String = ""

    constructor(source: Parcel){
        name = source.readString()
        id = source.readString()
        userid = source.readString()
        createTime = source.readLong()
        subscribedCount = source.readLong()
        coverImgUrl = source.readString()
        description = source.readString()
//        source.readStringArray(tags)
        playCount = source.readLong()
        shareCount = source.readInt()
        commentCount = source.readInt()
        bitmap = source.readString()
    }

    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        this.writeString(name)
        this.writeString(id)
        this.writeString(userid)
        this.writeLong(createTime)
        this.writeLong(subscribedCount)
        this.writeString(coverImgUrl)
        this.writeString(description)
//        this.writeStringArray(tags)
        this.writeLong(playCount)
        this.writeInt(shareCount)
        this.writeInt(commentCount)
        this.writeString(bitmap)

    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MusicList> = object : Parcelable.Creator<MusicList> {
            override fun createFromParcel(source: Parcel): MusicList = MusicList(source)
            override fun newArray(size: Int): Array<MusicList?> = arrayOfNulls(size)
        }
    }
}

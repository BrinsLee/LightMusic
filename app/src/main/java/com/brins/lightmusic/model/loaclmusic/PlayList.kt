package com.brins.lightmusic.model.loaclmusic

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayMode
import com.litesuits.orm.db.annotation.*
import com.litesuits.orm.db.enums.Relation
import java.util.*
import kotlin.collections.LinkedHashSet

/*
* 本地歌单
* */
@Entity(tableName = "PlayList")
class PlayList() : Parcelable {

    companion object {
        val NO_POSITION = -1
        val COLUMN_FAVORITE = "favorite"

        @JvmField
        val CREATOR: Parcelable.Creator<PlayList> = object : Parcelable.Creator<PlayList> {
            override fun createFromParcel(source: Parcel?): PlayList {
                return PlayList(source!!)
            }

            override fun newArray(size: Int): Array<PlayList?> {
                return arrayOfNulls(size)
            }

        }
    }

    constructor(song: Music) : this() {
        songs.add(song)
        numOfSongs = 1
    }

    @ColumnInfo(name = "Id")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private var id: Int = 0

    @ColumnInfo(name = "Name")
    private var name: String = ""

    @ColumnInfo(name = "Favorite")
    private var favorite: Boolean = false


    @MapCollection(ArrayList::class)
    @Mapping(Relation.OneToMany)
    private var songs: ArrayList<Music> = arrayListOf()

    @Ignore
    private var playingIndex = -1

    private var numOfSongs: Int = 0

    private var playMode: PlayMode? = PlayMode.LOOP

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        favorite = parcel.readByte() != 0.toByte()
        playingIndex = parcel.readInt()
        numOfSongs = parcel.readInt()
        songs = parcel.createTypedArrayList(Music)
        playingIndex = parcel.readInt()
        val tmpPlayMode = parcel.readInt()
        playMode = if (tmpPlayMode == -1) null else PlayMode.values()[tmpPlayMode]

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeByte(if (favorite) 1 else 0)
        parcel.writeInt(playingIndex)
        parcel.writeInt(numOfSongs)
        parcel.writeTypedList(this.songs)
        parcel.writeInt(this.playingIndex)
        parcel.writeInt(if (this.playMode == null) -1 else this.playMode!!.ordinal)
    }

    fun setPlayMode(playMode: PlayMode) {
        this.playMode = playMode
    }

    fun setPlayingIndex(playingIndex: Int) {
        this.playingIndex = playingIndex
    }

    fun getSongs(): MutableList<Music> {
        return songs
    }

    fun getPlayMode(): PlayMode {
        return playMode!!
    }

    fun getItemCount(): Int {
        return songs.size
    }

    fun addSong(@Nullable song: Music?) {
        if (song == null) return
        songs.add(song)
        numOfSongs = songs.size
    }

    fun addSong(@Nullable song: Music?, index: Int) {
        if (song == null) return

        songs.add(index, song)
        numOfSongs = songs.size
    }

    fun setSong(song: Music?, index: Int) {
        if (song == null) return
        songs[index] = song
    }

    fun addSong(@Nullable songs: List<Music>?, index: Int = 0) {
        if (songs == null || songs.isEmpty()) return
        this.songs.addAll(index, songs)
        val tempSet = LinkedHashSet<Music>(this.songs.size)
        tempSet.addAll(songs)
        this.songs.clear()
        this.songs.addAll(tempSet)
        this.numOfSongs = this.songs.size
    }

    fun removeSong(song: Music?): Boolean {
        if (song == null) return false

        val index: Int = songs.indexOf(song)
        if (index != -1) {
            numOfSongs = songs.size
            return true
        } else {
            val iterator = songs.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (song!!.fileName == item.fileName) {
                    iterator.remove()
                    numOfSongs = songs.size
                    return true
                }
            }
        }
        return false
    }

    fun prepare(): Boolean {
        if (songs.isEmpty()) return false
        if (playingIndex == NO_POSITION) {
            playingIndex = 0
        }
        return true
    }

    fun getCurrentSong(): Music? {
        return if (playingIndex != NO_POSITION) {
            songs[playingIndex]
        } else null
    }

    fun getNumOfSongs(): Int {
        return numOfSongs
    }

    fun hasLast(): Boolean {
        return songs.size != 0
    }

    fun hasNext(fromComplete: Boolean): Boolean {
        if (songs.isEmpty()) return false
        if (fromComplete) {
            if (playMode === PlayMode.LIST && playingIndex + 1 >= songs.size) return false
        }
        return true
    }

    fun last(): Music {
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.SINGLE -> {
                var newIndex = playingIndex - 1
                if (newIndex < 0) {
                    newIndex = songs.size - 1
                }
                playingIndex = newIndex
            }
            PlayMode.SHUFFLE -> playingIndex = randomPlayIndex()
        }
        return songs[playingIndex]
    }

    operator fun next(): Music {
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.SINGLE -> {
                var newIndex = playingIndex + 1
                if (newIndex >= songs.size) {
                    newIndex = 0
                }
                playingIndex = newIndex
            }
            PlayMode.SHUFFLE -> playingIndex = randomPlayIndex()
        }
        return songs[playingIndex]
    }

    fun getNext(): Music {
        var newIndex = 0
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.SINGLE -> {
                newIndex = playingIndex + 1
                if (newIndex >= songs.size) {
                    newIndex = 0
                }
            }
        }
        return songs[newIndex]
    }

    fun getLast(): Music {
        var newIndex = 0
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.SINGLE -> {
                newIndex = playingIndex - 1
                if (newIndex < 0) {
                    newIndex = songs.size - 1
                }
            }
        }
        return songs[newIndex]
    }
    private fun randomPlayIndex(): Int {
        val randomIndex = Random().nextInt(songs.size)
        // Make sure not play the same song twice if there are at least 2 data
        if (songs.size > 1 && randomIndex == playingIndex) {
            randomPlayIndex()
        }
        return randomIndex
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getPlayingIndex(): Int {
        return playingIndex
    }
}

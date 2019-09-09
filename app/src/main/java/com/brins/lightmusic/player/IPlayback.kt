package com.brins.lightmusic.player

import androidx.annotation.Nullable
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList

interface IPlayback {

    fun setPlayList(list : PlayList)

    fun play(): Boolean

    fun play(list: PlayList, startIndex: Int): Boolean

    fun play(song: Music): Boolean

    fun playLast(): Boolean

    fun playNext(): Boolean

    fun pause(): Boolean

    fun isPlaying(): Boolean

    fun getProgress(): Int

    fun getPlayingSong(): Music?

    fun seekTo(progress: Int): Boolean

    fun setPlayMode(playMode: PlayMode)

    fun registerCallback(callback: Callback)

    fun unregisterCallback(callback: Callback)

    fun removeCallbacks()

    fun releasePlayer()

    fun stop()

    interface Callback {

        fun onSwitchLast(@Nullable last: Music)

        fun onSwitchNext(@Nullable next: Music)

        fun onComplete(@Nullable next: Music?)

        fun onPlayStatusChanged(isPlaying: Boolean)
    }
}
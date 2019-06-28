package com.brins.lightmusic.player

import android.media.MediaPlayer
import android.provider.MediaStore
import android.util.Log
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList
import java.io.IOException
import java.util.ArrayList

class Player : IPlayback, MediaPlayer.OnCompletionListener {

    companion object{
        private val TAG = "Player"
        @Volatile
        private var sInstance: Player? = null
        @JvmStatic
        fun getInstance(): Player {
            if (sInstance == null) {
                synchronized(Player::class.java) {
                    if (sInstance == null) {
                        sInstance = Player()
                    }
                }
            }
            return sInstance!!
        }
    }

    private var mPlayer: MediaPlayer = MediaPlayer()
    private var mPlayList: PlayList = PlayList()
    // Default size 2: for service and UI
    private val mCallbacks = ArrayList<IPlayback.Callback>(2)

    // Player status
    private var isPaused: Boolean = false

    init {
        mPlayer.setOnCompletionListener(this)
    }
    override fun setPlayList(list: PlayList) {
        mPlayList = list
    }

    override fun play(): Boolean {
        if(isPaused){
            mPlayer.start()
            notifyPlayStatusChanged(true)
            return true
        }
        if(mPlayList.prepare()){
            var music = mPlayList.getCurrentSong()
            try {
                mPlayer.reset()
                mPlayer.setDataSource(music?.fileUrl)
                mPlayer.prepare()
                mPlayer.start()
                notifyPlayStatusChanged(true)
            }catch (e : IOException){
                Log.e(TAG, "play: ", e)
                notifyPlayStatusChanged(false)
                return false
            }
            return true
        }
        return false
    }

    private fun notifyPlayStatusChanged(isPlaying: Boolean) {
        for (callback in mCallbacks){
            callback.onPlayStatusChanged(isPlaying)
        }
    }

    override fun play(list: PlayList): Boolean {

        isPaused = false
        setPlayList(list)
        return play()
    }

    override fun play(list: PlayList, startIndex: Int): Boolean {

        if (list == null || startIndex < 0 || startIndex >= list.getNumOfSongs()){
            return false
        }
        isPaused = false
        list.setPlayingIndex(startIndex)
        setPlayList(list)
        return play()
    }

    override fun play(song: Music): Boolean {
        isPaused = false
        mPlayList.getSongs().clear()
        mPlayList.getSongs().add(song)
        return play()
    }

    override fun playLast(): Boolean {

        isPaused = false
        val hasLast = mPlayList.hasLast()
        if (hasLast) {
            val last = mPlayList.last()
            play()
            return true
        }
        return false
    }

    override fun playNext(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPlaying(): Boolean {
        return mPlayer.isPlaying
    }

    override fun getProgress(): Int {
        return mPlayer.currentPosition
    }

    override fun getPlayingSong(): Music? {
        return mPlayList.getCurrentSong()
    }

    override fun seekTo(progress: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayMode(playMode: PlayMode) {
        mPlayList.setPlayMode(playMode)
    }

    override fun registerCallback(callback: IPlayback.Callback) {
        mCallbacks.add(callback)
    }

    override fun unregisterCallback(callback: IPlayback.Callback) {
        mCallbacks.remove(callback)
    }

    override fun removeCallbacks() {
        mCallbacks.clear()
    }

    override fun releasePlayer() {
        mPlayer.reset()
        mPlayer.release()
        sInstance = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
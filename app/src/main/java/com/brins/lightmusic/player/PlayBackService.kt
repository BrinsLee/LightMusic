package com.brins.lightmusic.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList

class PlayBackService : Service(), IPlayback, IPlayback.Callback {

    companion object {
        @JvmStatic
        private val ACTION_PLAY_TOGGLE = "com.brins.lightmusic.ACTION.PLAY_TOGGLE"
        private val ACTION_PLAY_LAST = "com.brins.lightmusic.ACTION.PLAY_LAST"
        private val ACTION_PLAY_NEXT = "com.brins.lightmusic.ACTION.PLAY_NEXT"
        private val ACTION_STOP_SERVICE = "com.brins.lightmusic.ACTION.STOP_SERVICE"
        private val NOTIFICATION_ID = 1
    }

    private val mPlayer: Player by lazy { Player.getInstance() }
    private val mBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class LocalBinder : Binder() {
        val service: PlayBackService
            get() = this@PlayBackService
    }

    override fun onCreate() {
        super.onCreate()
        mPlayer.registerCallback(this)
    }

    override fun stopService(name: Intent): Boolean {
        stopForeground(true)
        unregisterCallback(this)
        return super.stopService(name)
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    //IPlayback
    override fun setPlayList(list: PlayList) {
        mPlayer.setPlayList(list)
    }

    override fun play(): Boolean {
        return mPlayer.play()
    }

    override fun play(list: PlayList): Boolean {
        return mPlayer.play(list)
    }

    override fun play(list: PlayList, startIndex: Int): Boolean {
        return mPlayer.play(list, startIndex)
    }

    override fun play(song: Music): Boolean {
        return mPlayer.play(song)
    }

    override fun playLast(): Boolean {
        return mPlayer.playLast()
    }

    override fun playNext(): Boolean {
        return mPlayer.playNext()
    }

    override fun pause(): Boolean {
        return mPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mPlayer.isPlaying()
    }

    override fun getProgress(): Int {
        return mPlayer.getProgress()
    }

    override fun getPlayingSong(): Music? {
        return mPlayer.getPlayingSong()
    }

    override fun seekTo(progress: Int): Boolean {
        return mPlayer.seekTo(progress)
    }

    override fun setPlayMode(playMode: PlayMode) {

        mPlayer.setPlayMode(playMode)
    }

    override fun registerCallback(callback: IPlayback.Callback) {

        mPlayer.registerCallback(callback)
    }

    override fun unregisterCallback(callback: IPlayback.Callback) {
        mPlayer.unregisterCallback(callback)
    }

    override fun removeCallbacks() {
        mPlayer.removeCallbacks()
    }

    override fun releasePlayer() {
        mPlayer.releasePlayer()
        super.onDestroy()
    }

    //Callback
    override fun onSwitchLast(last: Music) {
    }

    override fun onSwitchNext(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayStatusChanged(isPlaying: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

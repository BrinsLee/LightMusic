package com.brins.lightmusic.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList

class PlayBackService : Service() , IPlayback , IPlayback.Callback {

    companion object{
        @JvmStatic
        private val ACTION_PLAY_TOGGLE = "com.brins.lightmusic.ACTION.PLAY_TOGGLE"
        private val ACTION_PLAY_LAST = "com.brins.lightmusic.ACTION.PLAY_LAST"
        private val ACTION_PLAY_NEXT = "com.brins.lightmusic.ACTION.PLAY_NEXT"
        private val ACTION_STOP_SERVICE = "com.brins.lightmusic.ACTION.STOP_SERVICE"
        private val NOTIFICATION_ID = 1
    }

    private var mPlayer: Player? = null
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
        mPlayer = Player.getInstance()
        mPlayer!!.registerCallback(this)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play(list: PlayList): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play(list: PlayList, startIndex: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun play(song: Music): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun playLast(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun playNext(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isPlaying(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProgress(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlayingSong(): Music {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seekTo(progress: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayMode(playMode: PlayMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerCallback(callback: IPlayback.Callback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unregisterCallback(callback: IPlayback.Callback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeCallbacks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun releasePlayer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //Callback
    override fun onSwitchLast(last: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

package com.brins.lightmusic.ui.fragment.quickcontrol

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import io.reactivex.disposables.CompositeDisposable

class MusicPlayerPresenter(var context: Context, var mView: MusicPlayerContract.View) : MusicPlayerContract.Presenter{

    val mSubscriptions : CompositeDisposable = CompositeDisposable()
    private var mPlaybackService: PlayBackService? = null
    val mConnection : ServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            mPlaybackService = null
            mView.onPlaybackServiceUnbound()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlaybackService = (service as PlayBackService.LocalBinder).service
            mView.onPlaybackServiceBound(mPlaybackService!!)
            mView.onSongUpdated(mPlaybackService!!.getPlayingSong())
        }

    }

    init {
        mView.setPresenter(this)
    }
    override fun retrieveLastPlayMode() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSongAsFavorite(song: Music, favorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindPlaybackService() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unbindPlaybackService() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe() {
        bindPlaybackService()
        retrieveLastPlayMode()
        if (mPlaybackService != null && mPlaybackService!!.isPlaying()) {
            mView.onSongUpdated(mPlaybackService!!.getPlayingSong())
        } else {
        }
    }

    override fun unsubscribe() {

        unbindPlaybackService()
        mSubscriptions.clear()

    }
}
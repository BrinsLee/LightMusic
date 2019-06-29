package com.brins.lightmusic.ui.fragment.quickcontrol

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import io.reactivex.disposables.CompositeDisposable

class MusicPlayerPresenter(var context: Context, var mView: MusicPlayerContract.View) : MusicPlayerContract.Presenter{

    val mSubscriptions : CompositeDisposable = CompositeDisposable()
    private var mIsServiceBound: Boolean = false
    private var mPlaybackService: PlayBackService? = null
    val mConnection : ServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            mPlaybackService = null
            mView.onPlaybackServiceUnbound()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlaybackService = (service as PlayBackService.LocalBinder).service
            mView.onPlaybackServiceBound(mPlaybackService!!)
            if (mPlaybackService!!.getPlayingSong() == null){
                return
            }else{
                mView.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
            }
        }

    }

    init {
        mView.setPresenter(this)
    }
    override fun retrieveLastPlayMode() {

    }
    

    override fun setSongAsFavorite(song: Music, favorite: Boolean) {

    }

    override fun bindPlaybackService() {

        context.bindService(Intent(context, PlayBackService::class.java),mConnection , Context.BIND_AUTO_CREATE)
        mIsServiceBound = true
    }

    override fun unbindPlaybackService() {

        if (mIsServiceBound){
            context.unbindService(mConnection)
            mIsServiceBound = false
        }
    }

    override fun subscribe() {
        bindPlaybackService()
        retrieveLastPlayMode()
        if (mPlaybackService != null && mPlaybackService!!.isPlaying() && mPlaybackService!!.getPlayingSong() != null) {
            mView.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
        } else {
        }
    }

    override fun unsubscribe() {

        unbindPlaybackService()
        mSubscriptions.clear()

    }
}
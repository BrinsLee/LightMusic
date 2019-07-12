package com.brins.lightmusic.ui.fragment.quickcontrol

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import io.reactivex.disposables.CompositeDisposable

class MusicPlayerPresenter private constructor() : MusicPlayerContract.Presenter {

    companion object {
        @SuppressLint("StaticFieldLeak")
        val instance = SingletonHolder.holder

        private object SingletonHolder {
            @SuppressLint("StaticFieldLeak")
            val holder = MusicPlayerPresenter()
        }
    }

    private lateinit var mContext: Context
    private lateinit var mView: MusicPlayerContract.View
    val mSubscriptions: CompositeDisposable = CompositeDisposable()
    private var mIsServiceBound: Boolean = false
    private var mPlaybackService: PlayBackService? = null

    val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mPlaybackService = null
            mView.onPlaybackServiceUnbound()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlaybackService = (service as PlayBackService.LocalBinder).service
            mView.onPlaybackServiceBound(mPlaybackService!!)
            if (mPlaybackService!!.getPlayingSong() == null) {
                return
            } else {
                mView.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
            }
        }

    }

    @Synchronized
    fun setContext(context: Context): MusicPlayerPresenter {
        return if (::mContext.isInitialized) {
            this
        } else {
            this.mContext = context
            this
        }
    }

    @Synchronized
    fun setView(view: MusicPlayerContract.View): MusicPlayerPresenter {
        mView = view
        return this
    }

    override fun retrieveLastPlayMode() {

    }


    override fun setSongAsFavorite(song: Music, favorite: Boolean) {

    }

    override fun bindPlaybackService() {

        val intent = Intent(mContext, PlayBackService::class.java)
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        mIsServiceBound = true
        mContext.startService(intent)
    }

    override fun unbindPlaybackService() {

        if (mIsServiceBound) {
            mContext.unbindService(mConnection)
            mIsServiceBound = false
        }
    }

    override fun subscribe() {
        bindPlaybackService()
        retrieveLastPlayMode()
        if (mPlaybackService != null && mPlaybackService!!.getPlayingSong() != null) {
            mView.onPlaybackServiceBound(mPlaybackService!!)
            mView.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
        } else {
        }
    }

    override fun unsubscribe() {

        unbindPlaybackService()
        mSubscriptions.clear()

    }
}
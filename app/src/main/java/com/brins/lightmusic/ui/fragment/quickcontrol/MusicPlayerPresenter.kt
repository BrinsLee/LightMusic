package com.brins.lightmusic.ui.fragment.quickcontrol

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.utils.loadingOnlineCover
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MusicPlayerPresenter private constructor() : MusicPlayerContract.Presenter {
    override fun getOnLineCover(url: String) {
        val provider: AndroidLifecycleScopeProvider =
            AndroidLifecycleScopeProvider.from(mView.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

        Observable.create(ObservableOnSubscribe<Bitmap> {
            it.onNext(loadingOnlineCover(url))
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(provider)
            .subscribe {
                mView.onCoverLoad(it)
            }
    }

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
        mView.setPresenter(this)
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

//        unbindPlaybackService()
    }
}
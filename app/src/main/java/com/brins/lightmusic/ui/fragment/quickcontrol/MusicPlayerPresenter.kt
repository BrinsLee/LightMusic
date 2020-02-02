package com.brins.lightmusic.ui.fragment.quickcontrol

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayBackService.Companion.mIsServiceBound
import com.brins.lightmusic.utils.await
import com.brins.lightmusic.utils.loadingOnlineCover
import javax.inject.Inject

class MusicPlayerPresenter @Inject constructor() :
    MusicPlayerContract.Presenter {


    companion object {
        val instance = SingletonHolder.holder
        private var mPlaybackService: PlayBackService? = null

    }

    private object SingletonHolder {
        val holder = MusicPlayerPresenter()
    }

    private var mView: MusicPlayerContract.View? = null

    val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mPlaybackService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlaybackService = (service as PlayBackService.LocalBinder).service
            mView?.onPlaybackServiceBound(mPlaybackService!!)
            if (mPlaybackService!!.getPlayingSong() == null) {
                return
            } else {
                mView?.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
            }
        }

    }


/*    Observable.create(ObservableOnSubscribe<Bitmap> {
        it.onNext(loadingOnlineCover(onlineMusic.album.picUrl))
    }).compose(AsyncTransformer<Bitmap>())
    .concatMap { t ->
        onlineMusic.bitmapCover = t
        ApiHelper.getMusicService().getUrl(onlineMusic.id)
            .compose(AsyncTransformer<MusicBean>())
    }
    .autoDisposable(provider)
    .subscribe(object : DefaultObserver<MusicBean>() {
        override fun onSuccess(response: MusicBean) {
            val metaData = response
            if (metaData.data != null) {
                onlineMusic.fileUrl = metaData.data!![0].url
                mView!!.onMusicDetail(onlineMusic)
            }
        }

        override fun onFail(message: String) {
        }

    })*/

    override suspend fun loadMusicDetail(onlineMusic: Music): Music {
        onlineMusic.bitmapCover = loadingOnlineCover(onlineMusic.album.picUrl)
        val metaData = ApiHelper.getMusicService().getUrl(onlineMusic.id).await()
        if (metaData.data != null) {
            onlineMusic.fileUrl = metaData.data!![0].url
        }
        return onlineMusic
    }


    override fun retrieveLastPlayMode() {

    }


    override fun setSongAsFavorite(song: Music, favorite: Boolean) {

    }

    override fun bindPlaybackService() {

        if (!mIsServiceBound) {
            val mContext = BaseApplication.getInstance().baseContext
            val intent = Intent(mContext, PlayBackService::class.java)
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun unbindPlaybackService() {

        if (mIsServiceBound) {
            BaseApplication.getInstance().baseContext.unbindService(mConnection)
            mIsServiceBound = false
        }
    }


    override fun subscribe(view: MusicPlayerContract.View?) {
        mView = view
        bindPlaybackService()
        retrieveLastPlayMode()
        if (mPlaybackService != null && mPlaybackService!!.getPlayingSong() != null) {
            mView!!.onPlaybackServiceBound(mPlaybackService!!)
            mView!!.onSongUpdated(mPlaybackService!!.getPlayingSong()!!)
        }
    }

    override fun unsubscribe() {
        mView?.onPlaybackServiceUnbound()
//        unbindPlaybackService()
    }
}
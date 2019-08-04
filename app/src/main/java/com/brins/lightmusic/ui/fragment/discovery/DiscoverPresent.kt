package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.ApiHelper.getMusicUrl
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable


class DiscoverPresent(var mView: DiscoveryContract.View?) : DiscoveryContract.Presenter {

    val provider: AndroidLifecycleScopeProvider =
        AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadArtist() {
        ApiHelper.getArtist(12)
            .compose(AsyncTransformer<Data>())
            .autoDisposable(provider)
            .subscribe({ t ->
                if (t.artists != null && t.artists?.size != 0) {
                    mView!!.onArtistLoad(t.artists as MutableList<Artist>)
                }
            }, { t -> Log.d("LoadError", t.message) })
    }

    override fun loadMusicList() {
        ApiHelper.getPlayList(12)
            .compose(AsyncTransformer<Data>())
            .autoDisposable(provider)
            .subscribe { t ->
                if (t.playlists != null && t.playlists!!.isNotEmpty()) {
                    mView!!.onMusicListLoad(t.playlists as MutableList<MusicList>)
                }
                mView?.hideLoading()
            }
    }

    override fun loadMusicListDetail(id: String) {
        mView?.showLoading()
        ApiHelper.getPlayListDetail(id)
            .compose(AsyncTransformer<MusicListDetail>())
            .autoDisposable(provider)
            .subscribe { t ->
                if (t.playlist != null) {
                    mView!!.onDetailLoad(t.playlist!!)
                    mView!!.hideLoading()
                }
            }
    }

    override fun loadMusicDetail(onlineMusic: OnlineMusic) {
        var metaData: Songs
        mView!!.showLoading()
        getMusicUrl(onlineMusic.id)
            .compose(AsyncTransformer<Songs>())
            .autoDisposable(provider)
            .subscribe({
                metaData = it
                if (metaData.data != null) {
                    onlineMusic.fileUrl = metaData.data!![0].url
                    mView!!.onMusicDetail(onlineMusic)
                }
            }, {
                it.printStackTrace()
            })
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun subscribe() {
        mView?.showLoading()
        loadArtist()
        loadMusicList()
    }

    override fun unsubscribe() {
        mView = null
    }
}
package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.ApiHelper.getMusicUrl
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.onlinemusic.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable


class DiscoverPresent(var mView: DiscoveryContract.View?) : DiscoveryContract.Presenter {

    val provider: AndroidLifecycleScopeProvider =
        AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

    //    DefaultObserver
    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadArtist() {
        ApiHelper.getArtist(12)
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onSuccess(response: MusicListResult) {
                    if (response.artistBeans != null && response.artistBeans?.size != 0) {
                        mView!!.onArtistLoad(response.artistBeans as MutableList<ArtistBean>)
                    }else{
                        onFail("网络连接失败")
                    }
                }
                override fun onFinish() {
                    mView?.hideLoading()
                }
            })
    }

    override fun loadMusicList(top : Int) {
        ApiHelper.getPlayList(top)
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onFinish() {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListResult) {
                    if (response.playlists != null && response.playlists!!.isNotEmpty()) {
                        mView!!.onMusicListLoad(response.playlists as MutableList<MusicListBean>)
                    }
                }

            })
    }

    override fun loadMusicListDetail(id: String) {
        mView?.showLoading()
        ApiHelper.getPlayListDetail(id)
            .compose(AsyncTransformer<MusicListDetailResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListDetailResult>() {
                override fun onFinish() {
                    mView?.hideLoading()

                }

                override fun onSuccess(response: MusicListDetailResult) {
                    if (response.playlist != null) {
                        mView!!.onDetailLoad(response.playlist!!)
                    }
                }

            })
    }

    override fun loadMusicDetail(onlineMusic: OnlineMusic) {
        var metaData: MusicBean
        mView!!.showLoading()
        getMusicUrl(onlineMusic.id)
            .compose(AsyncTransformer<MusicBean>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicBean>() {
                override fun onFinish() {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicBean) {
                    metaData = response
                    if (metaData.data != null) {
                        onlineMusic.fileUrl = metaData.data!![0].url
                        mView!!.onMusicDetail(onlineMusic)
                    }
                }

            })
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun subscribe() {
        mView?.showLoading()
        mView?.setPresenter(this)
        loadArtist()
        loadMusicList()
    }

    override fun unsubscribe() {
        mView = null
    }
}
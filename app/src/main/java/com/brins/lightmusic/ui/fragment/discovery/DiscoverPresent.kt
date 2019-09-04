package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.banner.BannerResult
import com.brins.lightmusic.model.onlinemusic.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable


class DiscoverPresent private constructor() : DiscoveryContract.Presenter {


    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: DiscoveryContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = DiscoverPresent()
    }

    //    DefaultObserver
    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadBanner() {
        ApiHelper.getDiscoveryService().getBanner()
            .compose(AsyncTransformer<BannerResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<BannerResult>() {
                override fun onSuccess(response: BannerResult) {
                    if (response.bannners != null && response.bannners?.size != 0) {
                        mView!!.onBannerLoad(response.bannners!!)
                    } else {
                        onFail("网络连接失败")
                    }
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }
            })
    }

    override fun loadMusicList(top: Int) {
        ApiHelper.getPlayListService().getPlayList(top)
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListResult) {
                    if (response.playlists != null && response.playlists!!.isNotEmpty()) {
                        mView!!.onMusicListLoad(response.playlists as MutableList<MusicListBean>)
                        mView?.hideLoading()

                    }
                }

            })
    }

    override fun loadMusicListDetail(id: String) {
        mView?.showLoading()
        ApiHelper.getPlayListService().getPlayListDetail(id)
            .compose(AsyncTransformer<MusicListDetailResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListDetailResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListDetailResult) {
                    if (response.playlist != null) {
                        mView!!.onDetailLoad(response.playlist!!)
                        mView?.hideLoading()

                    }
                }

            })
    }

    override fun loadMusicDetail(onlineMusic: OnlineMusic) {
        var metaData: MusicBean
        mView!!.showLoading()
        ApiHelper.getMusicService().getUrl(onlineMusic.id)
            .compose(AsyncTransformer<MusicBean>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicBean>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicBean) {
                    metaData = response
                    if (metaData.data != null) {
                        onlineMusic.fileUrl = metaData.data!![0].url
                        mView!!.onMusicDetail(onlineMusic)
                        mView?.hideLoading()

                    }
                }

            })
    }

    override fun subscribe(view: DiscoveryContract.View) {
        mView = view
        mView?.showLoading()
        mView?.setPresenter(this)
        loadBanner()
        loadMusicList()
    }

    override fun unsubscribe() {
        mView = null
    }
}
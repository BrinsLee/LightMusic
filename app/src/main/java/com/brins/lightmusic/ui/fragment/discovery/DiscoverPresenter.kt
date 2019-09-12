package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.banner.BannerResult
import com.brins.lightmusic.model.onlinemusic.*
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryContract.Companion.TYPE_HIGHT
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryContract.Companion.TYPE_HOT
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable


class DiscoverPresenter private constructor() : DiscoveryContract.Presenter {


    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: DiscoveryContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = DiscoverPresenter()
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
        ApiHelper.getPlayListService().getHightQualityList(top)
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListResult) {
                    if (response.playlists != null && response.playlists!!.isNotEmpty()) {
                        mView!!.onMusicListLoad(
                            response.playlists as ArrayList<MusicListBean>,
                            TYPE_HIGHT
                        )
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


    override fun loadHotMusicList(top: Int) {
        ApiHelper.getPlayListService().getPlayList()
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListResult) {
                    if (response.playlists != null && response.playlists!!.isNotEmpty()) {
                        mView!!.onMusicListLoad(
                            response.playlists as ArrayList<MusicListBean>,
                            TYPE_HOT
                        )
                        mView?.hideLoading()

                    }
                }

            })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun subscribe(view: DiscoveryContract.View) {
        mView = view
        mView?.setPresenter(this)
    }

    fun initDiscoveryView(){
        mView?.showLoading()
        loadBanner()
        loadMusicList(6)
        loadHotMusicList(6)
    }


    override fun unsubscribe() {
        mView = null
    }
}
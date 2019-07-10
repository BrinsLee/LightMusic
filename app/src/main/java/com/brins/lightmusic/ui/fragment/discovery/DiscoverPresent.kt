package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.brins.lightmusic.model.Data
import com.brins.lightmusic.model.MusicListDetail
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DiscoverPresent(var mView: DiscoveryContract.View?) : DiscoveryContract.Presenter {


    val provider: AndroidLifecycleScopeProvider =
        AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadArtist() {
        ApiHelper.getArtist(12)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .autoDisposable(provider)
            .subscribe(object : Observer<Data> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Data) {
                    if (t.artists != null && t.artists?.size != 0) {
                        mView!!.onArtistLoad(t.artists as MutableList<Artist>)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("LoadError", e.message)
                }
            })
    }

    override fun loadMusicList() {
        ApiHelper.getPlayList(12)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .autoDisposable(provider)
            .subscribe(object : Observer<Data> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Data) {
                    if (t.playlists != null && t.playlists!!.isNotEmpty()) {
                        mView!!.onMusicListLoad(t.playlists as MutableList<MusicList>)
                    }
                    mView?.hideLoading()
                }

                override fun onError(e: Throwable) {}

            })
    }

    override fun loadMusicListDetail(id : String) {
        mView?.showLoading()
        ApiHelper.getPlayListDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(provider)
            .subscribe(object :Observer<MusicListDetail>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: MusicListDetail) {
                    if (t.playlist != null){
                        mView!!.onDetailLoad(t.playlist!!)
                        mView!!.hideLoading()
                    }
                }

                override fun onError(e: Throwable) {}

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
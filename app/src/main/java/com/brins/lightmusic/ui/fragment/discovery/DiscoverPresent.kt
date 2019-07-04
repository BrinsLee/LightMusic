package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.Data
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DiscoverPresent (var mView : DiscoveryContract.View?): DiscoveryContract.Presenter {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadArtist() {
        ApiHelper.getArtist(12)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<Data>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Data) {
                    if (t.artists != null && t.artists?.size != 0){
                        mView!!.onArtistLoad(t.artists as MutableList<Artist>)
                    }
                }
                override fun onError(e: Throwable) {
                    Log.d("LoadError",e.message)
                }
            })
    }

    override fun loadMusicList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun subscribe() {
        loadArtist()
    }

    override fun unsubscribe() {
        mView = null
    }
}
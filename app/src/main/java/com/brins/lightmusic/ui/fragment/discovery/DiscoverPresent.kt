package com.brins.lightmusic.ui.fragment.discovery

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.PlayList
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class DiscoverPresent (var mView : DiscoveryContract.View?): DiscoveryContract.Presenter {


    val mSubscriptions: CompositeDisposable = CompositeDisposable()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadArtist() {
        ApiHelper.getArtist(12)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .autoDisposable(AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY))
            .subscribe(object : Observer<MutableList<Artist>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: MutableList<Artist>) {
                    if (t.size != 0){
                        mView!!.onArtistLoad(t)
                    }
                }
                override fun onError(e: Throwable) {
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
        mSubscriptions.clear()
    }
}
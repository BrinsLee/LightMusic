package com.brins.lightmusic.ui.fragment.artists

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.musicvideo.MvResult
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class ArtistDetailPresenter : ArtistDetailConstract.Presenter {

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: ArtistDetailConstract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ArtistDetailPresenter()
    }


    override fun loadArtistSong(id: String) {
        mView?.showLoading()
        ApiHelper.getArtistService().getArtistMusic(id)
            .compose(AsyncTransformer<ArtistSongResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<ArtistSongResult>() {
                override fun onSuccess(response: ArtistSongResult) {
                    mView?.hideLoading()
                    if (!response.hot.isNullOrEmpty())
                        mView?.onArtistSongLoad(response)
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    override fun loadArtistAlbum(id: String) {
    }

    override fun loadArtistMv(id: String, limit: Int) {
        mView?.showLoading()
        ApiHelper.getArtistService().getArtistMV(id,limit).compose(AsyncTransformer<MvResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MvResult>(){
                override fun onSuccess(response: MvResult) {
                    mView?.hideLoading()
                    if (response.dataBeans != null && response.dataBeans!!.isNotEmpty()){
                        mView?.onArtistMvLoad(response)
                    }
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    override fun subscribe(view: ArtistDetailConstract.View?) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }
}
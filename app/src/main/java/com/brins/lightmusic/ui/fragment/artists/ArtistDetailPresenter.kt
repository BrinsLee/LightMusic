package com.brins.lightmusic.ui.fragment.artists

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.album.AlbumListResult
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvMetaResult
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
        mView?.showLoading()
        ApiHelper.getArtistService().getArtistAlbum(id).compose(AsyncTransformer<AlbumListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<AlbumListResult>() {
                override fun onSuccess(response: AlbumListResult) {
                    mView?.hideLoading()
                    if (response.hotAlbums != null && response.hotAlbums!!.isNotEmpty()) {
                        mView?.onArtistAlbumLoad(response)
                    }
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    override fun loadArtistMv(id: String, limit: Int) {
        mView?.showLoading()
        ApiHelper.getArtistService().getArtistMV(id, limit).compose(AsyncTransformer<MvResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MvResult>() {
                override fun onSuccess(response: MvResult) {
                    if (response.dataBeans != null && response.dataBeans!!.isNotEmpty()) {
                        mView?.hideLoading()
                        mView?.onArtistMvLoad(response)
                    }
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    fun loadUrl(dataBean: LastestMvDataBean) {
        mView?.showLoading()
        ApiHelper.getMvService().getMvMetaData(dataBean.id)
            .compose(AsyncTransformer<MvMetaResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MvMetaResult>(){
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MvMetaResult) {
                    if (response.dataBean != null) {
                            mView?.hideLoading()
                            mView?.onArtistMvDetailLoad(Mv(dataBean,response.dataBean!!))
                        }
                    }
                }
            )

    }

    override fun subscribe(view: ArtistDetailConstract.View?) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }
}
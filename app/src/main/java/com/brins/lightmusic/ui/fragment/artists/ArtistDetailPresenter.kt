package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.utils.await
import javax.inject.Inject

class ArtistDetailPresenter @Inject constructor() : ArtistDetailConstract.Presenter {

    private var mView: ArtistDetailConstract.View? = null

    override suspend fun loadArtistSong(id: String) =
        ApiHelper.getArtistService().getArtistMusic(id).await()

    override suspend fun loadArtistAlbum(id: String) =
        ApiHelper.getArtistService().getArtistAlbum(id).await()

    override suspend fun loadArtistMv(id: String, limit: Int) =
        ApiHelper.getArtistService().getArtistMV(id, limit).await()

//    fun loadUrl(dataBean: LastestMvDataBean) {
//        mView?.showLoading()
//        ApiHelper.getMvService().getMvMetaData(dataBean.id)
//            .compose(AsyncTransformer<MvMetaResult>())
//            .autoDisposable(provider)
//            .subscribe(object : DefaultObserver<MvMetaResult>(){
//                override fun onFail(message: String) {
//                    mView?.hideLoading()
//                }
//
//                override fun onSuccess(response: MvMetaResult) {
//                    if (response.dataBean != null) {
//                            mView?.hideLoading()
//                            mView?.onArtistMvDetailLoad(Mv(dataBean,response.dataBean!!))
//                        }
//                    }
//                }
//            )
//
//    }

    override suspend fun loadUrl(dataBean: LastestMvDataBean): Mv {
        val result = ApiHelper.getMvService().getMvMetaData(dataBean.id).await()
        return Mv(dataBean, result.dataBean!!)
    }

    override fun subscribe(view: ArtistDetailConstract.View?) {
        mView = view
    }

    override fun unsubscribe() {
        mView = null
    }
}
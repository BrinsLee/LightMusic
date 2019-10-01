package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.musicvideo.*
import com.brins.lightmusic.utils.await

class VideoPresent(var mView: VideoContract.View?) : VideoContract.Presenter {


    private val mvList = mutableListOf<Mv>()
/*
    override fun loadVideo(limit: Int, area: String) {
        mView?.showLoading()
        ApiHelper.getMvService().getMvAll(area, limit)
            .compose(AsyncTransformer<MvResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MvResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MvResult) {
                    if (response.dataBeans != null && response.dataBeans!!.isNotEmpty()) {
                        val num = response.dataBeans!!.size
                        response.dataBeans!!.forEach {
                            loadUrl(it,
                                Consumer { t ->
                                    if (t.dataBean != null) {
                                        mvList.add(Mv(it, t.dataBean!!))
                                        if (mvList.size == num) {
                                            mView?.onVideoLoad(mvList)
                                            mView?.hideLoading()

                                        }
                                    }
                                })
                        }
                    }
                }
            })
    }
*/

    override suspend fun loadVideo(limit: Int, area: String): List<Mv> {
        val mvResult = ApiHelper.getMvService().getMvAll(area, limit).await()
        if (mvResult.dataBeans != null && mvResult.dataBeans!!.isNotEmpty()) {
            mvResult.dataBeans!!.forEach {
                val t =loadUrl(it)
                if (t.dataBean != null) {
                    mvList.add(Mv(it, t.dataBean!!))
                }
            }
        }
        return mvList
    }

    @SuppressLint("CheckResult")
    override suspend fun loadUrl(dataBean: LastestMvDataBean) = ApiHelper.getMvService().getMvMetaData(dataBean.id).await()


    override suspend fun loadVideoComments(id: String) = ApiHelper.getMvService().getMvComments(id).await()


    override fun subscribe(view: VideoContract.View?) {
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }


}
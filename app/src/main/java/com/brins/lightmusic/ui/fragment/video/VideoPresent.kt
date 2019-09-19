package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.musicvideo.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.functions.Consumer


class VideoPresent(var mView: VideoContract.View?) : VideoContract.Presenter {



    val provider: AndroidLifecycleScopeProvider =
        AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

    val mvList = mutableListOf<Mv>()
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

    @SuppressLint("CheckResult")
    override fun loadUrl(dataBean: LastestMvDataBean, consumer: Consumer<MvMetaResult>) {
        ApiHelper.getMvService().getMvMetaData(dataBean.id)
            .compose(AsyncTransformer<MvMetaResult>())
            .subscribe(consumer)

    }

    override fun loadVideoComments(id: String) {
        mView?.showLoading()
        ApiHelper.getMvService().getMvComments(id).compose(AsyncTransformer<MvCommentsResult>())
            .subscribe(object : DefaultObserver<MvCommentsResult>(){
                override fun onSuccess(response: MvCommentsResult) {
                    mView?.hideLoading()
                    mView?.onVideoCommomLoad(response)
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    override fun subscribe(view: VideoContract.View?) {
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }


}
package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.model.musicvideo.MvResult
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.functions.Consumer


class VideoPresent(var mView: VideoContract.View?) : VideoContract.Presenter {


    val provider: AndroidLifecycleScopeProvider =
        AndroidLifecycleScopeProvider.from(mView!!.getLifeActivity(), Lifecycle.Event.ON_DESTROY)

    val mvList = mutableListOf<Mv>()
    override fun loadVideo(limit: Int) {
        ApiHelper.getMvService().getLatestMusicVideo(limit)
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

    override fun subscribe(view: VideoContract.View?) {
        mView?.showLoading()
        mView?.setPresenter(this)
        loadVideo()
    }

    override fun unsubscribe() {
        mView = null
    }


}
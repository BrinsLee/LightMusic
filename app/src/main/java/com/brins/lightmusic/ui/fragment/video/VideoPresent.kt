package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.common.AsyncTransformer
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
    override fun loadVideo() {
        ApiHelper.getLatestMvData()
            .compose(AsyncTransformer<MvResult>())
            .autoDisposable(provider)
            .subscribe({ t ->
                if (t.dataBeans != null && t.dataBeans!!.isNotEmpty()) {
                    val num = t.dataBeans!!.size
                    t.dataBeans!!.forEach {
                        loadUrl(it.id,
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
            }, { t -> Log.d("error", t.message) })
    }

    @SuppressLint("CheckResult")
    override fun loadUrl(id: String, consumer: Consumer<MvMetaResult>) {
        ApiHelper.getMvMetaData(id).compose(AsyncTransformer<MvMetaResult>())
            .subscribe(consumer)

    }

    override fun subscribe() {
        mView?.showLoading()
        loadVideo()
    }

    override fun unsubscribe() {
        mView = null
    }


}
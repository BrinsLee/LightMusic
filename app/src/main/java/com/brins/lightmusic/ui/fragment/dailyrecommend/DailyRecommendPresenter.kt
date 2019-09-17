package com.brins.lightmusic.ui.fragment.dailyrecommend

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.dailyrecommend.DailyRecommendResult
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class DailyRecommendPresenter : DailyContract.Presenter {

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: DailyContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = DailyRecommendPresenter()
    }

    override fun loadDailyRecommend() {
        mView?.showLoading()
        ApiHelper.getUserPlayListService().getDailyRecommend()
            .compose(AsyncTransformer<DailyRecommendResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<DailyRecommendResult>() {
                override fun onSuccess(response: DailyRecommendResult) {
                    if (response.recommend != null) {
                        mView?.onMusicLoad(response)
                        mView?.hideLoading()
                    }
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

            })
    }

    override fun subscribe(view: DailyContract.View?) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }
}
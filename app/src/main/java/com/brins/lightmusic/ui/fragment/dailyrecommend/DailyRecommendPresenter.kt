package com.brins.lightmusic.ui.fragment.dailyrecommend

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.dailyrecommend.DailyRecommendResult
import com.brins.lightmusic.utils.await

class DailyRecommendPresenter : DailyContract.Presenter {

/*    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }*/
    private var mView: DailyContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = DailyRecommendPresenter()
    }

    override suspend fun loadDailyRecommend() :DailyRecommendResult = ApiHelper.getUserPlayListService().getDailyRecommend().await()
    /*{
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
    }*/

    override fun subscribe(view: DailyContract.View?) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }
}
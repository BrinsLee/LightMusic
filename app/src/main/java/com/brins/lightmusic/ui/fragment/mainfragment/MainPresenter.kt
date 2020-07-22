package com.brins.lightmusic.ui.fragment.mainfragment

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.utils.await
import javax.inject.Inject


/**
 * @author lipeilin
 * @date 2020/7/22
 */
class MainPresenter @Inject constructor() : MainContract.Presenter {

    private var mView: MainContract.View? = null


    override suspend fun loadPersonalizedMusic() {
        val result = ApiHelper.getPersonalizedService().getPersonalizedMusic().await()
        mView?.let {
            it.onPersonalizedMusicLoad(result.result)
        }
    }


    override fun subscribe(view: MainContract.View?) {
        mView = view
    }

    override fun unsubscribe() {
        mView = null
    }
}
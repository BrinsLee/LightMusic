package com.brins.lightmusic.ui.fragment.usermusiclist

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.utils.await
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class MusicListPresenter : MusicListContract.Presenter {


    private var mView: MusicListContract.View? = null

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }

    companion object {
        val instance = SingletonHolder.holder

    }

    private object SingletonHolder {
        val holder = MusicListPresenter()
    }


    override suspend fun loadMusicList(id: String) = ApiHelper.getPlayListService().getPlayListDetail(id).await()

    override fun subscribe(view: MusicListContract.View) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }


}
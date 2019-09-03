package com.brins.lightmusic.ui.activity.usermusiclist

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.onlinemusic.MusicBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.activity.login.LoginPresenter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class MusicListPresenter : MusicListContract.Presenter {

    val mMusicListService = ApiHelper.getPlayListService()

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


    override fun loadMusicDetail(onlineMusic: OnlineMusic) {
        var metaData: MusicBean
        mView!!.showLoading()
        ApiHelper.getMusicService().getUrl(onlineMusic.id)
            .compose(AsyncTransformer<MusicBean>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicBean>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicBean) {
                    metaData = response
                    if (metaData.data != null) {
                        onlineMusic.fileUrl = metaData.data!![0].url
                        mView!!.onMusicDetail(onlineMusic)
                        mView?.hideLoading()

                    }
                }

            })
    }


    override fun loadMusicList(id: String) {
        mView?.showLoading()
        mMusicListService.getPlayListDetail(id)
            .compose(AsyncTransformer<MusicListDetailResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListDetailResult>() {
                override fun onFail(message: String) {
                    mView?.hideLoading()
                }

                override fun onSuccess(response: MusicListDetailResult) {
                    if (response.playlist != null) {
                        mView!!.onMusicListLoad(response.playlist!!)
                    }
                }

            })
    }

    override fun subscribe(view: MusicListContract.View) {
        mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }


}
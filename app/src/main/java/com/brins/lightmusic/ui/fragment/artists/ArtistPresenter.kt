package com.brins.lightmusic.ui.fragment.artists

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import com.brins.lightmusic.utils.JsonToObject
import com.brins.lightmusic.utils.getJson
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class ArtistPresenter private constructor() : ArtistConstract.Presenter {

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }
    private var mView: ArtistConstract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ArtistPresenter()
    }

    override fun loadArtistCategory() {
        mView?.showLoading()
        val json = getJson(BaseApplication.getInstance().baseContext, "category.json")
        mView?.onArtistCategoryLoad(JsonToObject(json, CategoryResult::class.java))
        loadArtist()
    }

    override fun loadArtist() {
        ApiHelper.getArtistService().getArtist()
            .compose(AsyncTransformer<MusicListResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<MusicListResult>() {
                override fun onSuccess(response: MusicListResult) {
                    if (response.artistBeans != null && response.artistBeans?.size != 0) {
                        mView!!.onArtistLoad(response.artistBeans as ArrayList<ArtistBean>)
                    } else {
                        onFail("网络连接失败")
                    }
                    mView?.hideLoading()
                }

                override fun onFail(message: String) {
                    mView?.hideLoading()
                }
            })
    }

    override fun subscribe(view: ArtistConstract.View?) {
        mView = view
        mView!!.setPresenter(this)
        loadArtistCategory()
    }

    override fun unsubscribe() {
        mView = null
    }

}
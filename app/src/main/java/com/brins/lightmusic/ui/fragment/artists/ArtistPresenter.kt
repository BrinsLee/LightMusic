package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.model.artist.CategoryResultData
import com.brins.lightmusic.utils.JsonToObject
import com.brins.lightmusic.utils.await
import com.brins.lightmusic.utils.getJson

class ArtistPresenter private constructor() : ArtistConstract.Presenter {



    override suspend fun loadArtistCategory(type: Int): CategoryResultData = ApiHelper.getArtistService().getArtistCategory(type).await()

    override suspend fun loadArtist(): ArrayList<ArtistBean> = ApiHelper.getArtistService().getArtist().await().artistBeans as ArrayList<ArtistBean>

    override suspend fun loadArtistCategory(): CategoryResult {
        val json = getJson(BaseApplication.getInstance().baseContext, "category.json")
        return JsonToObject(json, CategoryResult::class.java)
    }

    private var mView: ArtistConstract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = ArtistPresenter()
    }

/*
    override fun loadArtistCategory() {
        mView?.showLoading()
        val json = getJson(BaseApplication.getInstance().baseContext, "category.json")
        mView?.onArtistCategoryLoad(JsonToObject(json, CategoryResult::class.java))
        loadArtist()
    }
*/

   /* override fun loadArtist() {
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
    }*/

    override fun subscribe(view: ArtistConstract.View?) {
        mView = view
        mView!!.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }

}
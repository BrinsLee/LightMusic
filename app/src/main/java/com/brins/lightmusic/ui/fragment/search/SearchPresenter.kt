package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.utils.await
import javax.inject.Inject

class SearchPresenter @Inject constructor() : SearchContract.Presenter {

    private var mView: SearchContract.View? = null
    private val mvList = mutableListOf<Mv>()


    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SearchPresenter()
    }

    override suspend fun searchMusicData(input: String, type: Int) =
        ApiHelper.getSearchService().searchMusic(input, type).await()

    override suspend fun loadSearchHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun searchAlbumData(input: String, type: Int) =
        ApiHelper.getSearchService().searchAlbum(input, type).await()

    override suspend fun searchArtistData(input: String) =
        ApiHelper.getSearchService().searchArtist(input).await()

    override suspend fun loadSearchSuggest(input: String) =
        ApiHelper.getSearchService().searchSuggest(input).await()

    override suspend fun searchMusicListData(input: String) =
        ApiHelper.getSearchService().searchMusicList(input).await()

    override suspend fun searchMusicVideoData(input: String): List<Mv> {
        mvList.clear()
        val mvResult = ApiHelper.getSearchService().searchMusicVideo(input).await()
        if (mvResult.dataBean != null && mvResult.dataBean?.data != null && mvResult.dataBean?.data!!.isNotEmpty()) {
            mvResult.dataBean!!.data!!.forEach {
                val t = loadUrl(it)
                if (t.dataBean != null) {
                    mvList.add(Mv(it, t.dataBean!!))
                }
            }
        }
        return mvList
    }

    suspend fun loadUrl(dataBean: LastestMvDataBean) =
        ApiHelper.getMvService().getMvMetaData(dataBean.id).await()


    override fun subscribe(view: SearchContract.View?) {
        this.mView = view
    }

    override fun unsubscribe() {
        mView = null
    }
}
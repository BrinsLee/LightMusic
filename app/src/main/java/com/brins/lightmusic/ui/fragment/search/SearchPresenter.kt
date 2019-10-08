package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.search.SearchResult
import com.brins.lightmusic.utils.await

class SearchPresenter private constructor(): SearchContract.Presenter {



    private var mView: SearchContract.View? = null

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SearchPresenter()
    }
    override suspend fun searchMusicData(input: String, type : Int) = ApiHelper.getSearchService().searchMusic(input, type).await()

    override suspend fun loadSearchHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun searchAlbumData(input: String, type: Int) = ApiHelper.getSearchService().searchAlbum(input, type).await()

    override suspend fun loadSearchSuggest(input: String) = ApiHelper.getSearchService().searchSuggest(input).await()

    override fun subscribe(view: SearchContract.View?) {
        this.mView = view
        mView?.setPresenter(this)
    }

    override fun unsubscribe() {
        mView = null
    }
}
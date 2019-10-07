package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.utils.await

class SearchPresenter private constructor(): SearchContract.Presenter {



    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SearchPresenter()
    }
    override suspend fun <T> searchData(input: String, type : Int) = ApiHelper.getSearchService().search<T>(input, type).await()

    override suspend fun loadSearchHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override suspend fun loadSearchSuggest(input: String) = ApiHelper.getSearchService().searchSuggest(input).await()

    override fun subscribe(view: SearchContract.View?) {

    }

    override fun unsubscribe() {

    }
}
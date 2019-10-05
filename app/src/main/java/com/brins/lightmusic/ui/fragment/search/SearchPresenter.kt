package com.brins.lightmusic.ui.fragment.search

class SearchPresenter private constructor(): SearchContract.Presenter {


    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SearchPresenter()
    }
    override suspend fun searchData(input: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun loadSearchHistory() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribe(view: SearchContract.View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unsubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
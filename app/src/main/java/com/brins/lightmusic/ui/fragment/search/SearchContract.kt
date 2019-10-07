package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.model.search.SearchSuggestResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface SearchContract {

    interface View: BaseView<Presenter>

    interface Presenter: BasePresenter<View>{
        suspend fun <T> searchData(input : String, type: Int = 1): T

        suspend fun loadSearchHistory()

        suspend fun loadSearchSuggest(input : String): SearchSuggestResult
    }
}
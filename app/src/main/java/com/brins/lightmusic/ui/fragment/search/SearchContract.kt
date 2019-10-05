package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface SearchContract {

    interface View: BaseView<Presenter>

    interface Presenter: BasePresenter<View>{
        suspend fun searchData(input : String)

        suspend fun loadSearchHistory()
    }
}
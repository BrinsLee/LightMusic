package com.brins.lightmusic.ui.fragment.search

import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.search.SearchResult
import com.brins.lightmusic.model.search.SearchSuggestResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface SearchContract {

    interface View: BaseView<Presenter>

    interface Presenter: BasePresenter<View>{
        suspend fun searchMusicData(input : String, type: Int = 1): SearchResult<Music>

        suspend fun searchAlbumData(input: String, type: Int): SearchResult<Album>

        suspend fun loadSearchHistory()

        suspend fun loadSearchSuggest(input : String): SearchSuggestResult
    }
}
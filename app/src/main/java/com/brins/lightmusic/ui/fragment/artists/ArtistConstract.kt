package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistConstract {
    interface View : BaseView<Presenter> {


    }

    interface Presenter : BasePresenter<View> {

        suspend fun loadArtistCategory(): CategoryResult

        suspend fun loadArtist(): ArrayList<ArtistBean>

    }
}
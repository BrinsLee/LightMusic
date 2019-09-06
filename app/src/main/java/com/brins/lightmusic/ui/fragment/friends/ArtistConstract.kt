package com.brins.lightmusic.ui.fragment.friends

import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.artist.Category
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistConstract {
    interface View : BaseView<Presenter>{

        fun onArtistLoad(artistList : ArrayList<ArtistBean>)

        fun onArtistCategoryLoad(category : CategoryResult)

        fun handleError(error: Throwable)

        fun showLoading()

        fun hideLoading()

    }
    interface Presenter : BasePresenter<View>{

        fun loadArtistCategory()

        fun loadArtist()
    }
}
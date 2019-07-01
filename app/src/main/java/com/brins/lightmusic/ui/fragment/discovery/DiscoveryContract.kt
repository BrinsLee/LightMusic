package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DiscoveryContract {

    interface View : BaseView<Presenter>{

        fun getcontext() : Context

        fun handleError(error: Throwable)

        fun onMusicListLoad(songs : MutableList<Music>)

        fun onArtistLoad(artists : MutableList<Artist>)
    }
    interface Presenter : BasePresenter{
        fun loadArtist()

        fun loadMusicList()
    }
}
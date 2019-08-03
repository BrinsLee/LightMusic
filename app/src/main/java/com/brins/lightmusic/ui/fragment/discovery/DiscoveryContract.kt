package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.model.*
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DiscoveryContract {

    interface View : BaseView<Presenter>{

        fun showLoading()

        fun hideLoading()

        fun getcontext() : Context

        fun handleError(error: Throwable)

        fun onMusicListLoad(songs : MutableList<MusicList>)

        fun onArtistLoad(artists : MutableList<Artist>)

        fun onDetailLoad(detail : PlayListDetail)

        fun onMusicDetail(onlineMusic: OnlineMusic)
    }
    interface Presenter : BasePresenter{
        fun loadArtist()

        fun loadMusicList()

        fun loadMusicListDetail(id : String)

        fun loadMusicDetail(onlineMusic: OnlineMusic)
    }
}
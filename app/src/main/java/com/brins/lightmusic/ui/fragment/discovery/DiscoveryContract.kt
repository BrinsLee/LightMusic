package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DiscoveryContract {

    interface View : BaseView<Presenter>{

        fun showLoading()

        fun hideLoading()

        fun getcontext() : Context

        fun handleError(error: Throwable)

        fun onMusicListLoad(songs : MutableList<MusicListBean>)

        fun onArtistLoad(artistBeans : MutableList<ArtistBean>)

        fun onDetailLoad(detailBean : MusicListDetailBean)

        fun onMusicDetail(onlineMusic: OnlineMusic)
    }
    interface Presenter : BasePresenter{
        fun loadArtist()

        fun loadMusicList()

        fun loadMusicListDetail(id : String)

        fun loadMusicDetail(onlineMusic: OnlineMusic)
    }
}
package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DiscoveryContract {

    companion object{
        val TYPE_HIGHT = 1
        val TYPE_HOT = 2
    }
    interface View : BaseView<Presenter>{

        fun showLoading()

        fun hideLoading()

//        fun getcontext() : Context

        fun handleError(error: Throwable)

        fun onMusicListLoad(songs : ArrayList<MusicListBean>,type : Int)

        fun onBannerLoad(banners : ArrayList<Banner>)

        fun onDetailLoad(detailBean : MusicListDetailBean)

        fun onMusicDetail(onlineMusic: OnlineMusic)
    }
    interface Presenter : BasePresenter<View>{
        fun loadBanner()

        fun loadMusicList(top : Int)

        fun loadMusicListDetail(id : String)

        fun loadHotMusicList(top : Int)
    }
}
package com.brins.lightmusic.ui.fragment.discovery

import com.brins.lightmusic.model.album.AlbumResult
import com.brins.lightmusic.model.banner.BannerResult
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.model.onlinemusic.MusicListResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DiscoveryContract {


    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
        suspend fun loadBanner(): BannerResult

        suspend fun loadMusicList(top: Int) : MusicListResult

        suspend fun loadMusicListDetail(id: String) : MusicListDetailResult

        suspend fun loadHotMusicList(top: Int) : MusicListResult

        suspend fun loadAlbumDetail(id: String) : AlbumResult
    }
}
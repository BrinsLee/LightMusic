package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistDetailConstract {
    interface View : BaseView<Presenter>{
        fun onArtistInfoLoad()

        fun onArtistSongLoad()

        fun onArtistMvLoad()

        fun onArtistAlbumLoad()
    }
    interface Presenter : BasePresenter<View>{
        fun loadArtistInfo()

        /*
        * 获取歌手单曲
        * */
        fun loadArtistSong(id: String)

        fun loadArtistAlbum(id: String)

        fun loadArtistMv(id: String,limit : Int)
    }
}
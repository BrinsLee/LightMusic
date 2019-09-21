package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistDetailConstract {
    interface View : BaseView<Presenter>{

        fun onArtistSongLoad(result: ArtistSongResult)

        fun onArtistMvLoad(result: MvResult)

        fun onArtistAlbumLoad()
    }
    interface Presenter : BasePresenter<View>{

        /*
        * 获取歌手单曲
        * */
        fun loadArtistSong(id: String)

        fun loadArtistAlbum(id: String)

        fun loadArtistMv(id: String,limit : Int)
    }
}
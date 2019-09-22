package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.model.album.AlbumListResult
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistDetailConstract {
    interface View : BaseView<Presenter>{

        fun onArtistSongLoad(result: ArtistSongResult)

        fun onArtistMvLoad(result: MutableList<Mv>)

        fun onArtistAlbumLoad(response: AlbumListResult)
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
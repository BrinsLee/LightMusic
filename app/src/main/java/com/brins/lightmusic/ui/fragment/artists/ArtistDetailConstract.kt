package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.model.album.AlbumListResult
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface ArtistDetailConstract {
    interface View : BaseView{



    }
    interface Presenter : BasePresenter<View>{

        /*
        * 获取歌手单曲
        * */
        suspend fun loadArtistSong(id: String): ArtistSongResult

        suspend fun loadArtistAlbum(id: String): AlbumListResult

        suspend fun loadArtistMv(id: String,limit : Int): MvResult

        suspend fun loadUrl(dataBean: LastestMvDataBean): Mv
    }
}
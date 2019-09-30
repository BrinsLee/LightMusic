package com.brins.lightmusic.ui.fragment.usermusiclist

import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MusicListContract {
    interface View : BaseView<Presenter>{



    }
    interface Presenter : BasePresenter<View>{

        suspend fun loadMusicList(id : String): MusicListDetailResult


    }

}
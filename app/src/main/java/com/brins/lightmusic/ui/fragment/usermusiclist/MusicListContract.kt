package com.brins.lightmusic.ui.fragment.usermusiclist

import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MusicListContract {
    interface View : BaseView<Presenter>{

        fun onMusicListLoad(detailBean : MusicListDetailBean)

        fun onLoadFail()


    }
    interface Presenter : BasePresenter<View>{

        fun loadMusicList(id : String)



    }

}
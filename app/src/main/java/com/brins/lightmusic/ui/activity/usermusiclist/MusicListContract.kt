package com.brins.lightmusic.ui.activity.usermusiclist

import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MusicListContract {
    interface View : BaseView<Presenter>{
        fun handleError(error : Throwable)

        fun showLoading()

        fun hideLoading()

        fun onMusicListLoad(detailBean : MusicListDetailBean)

        fun onLoadFail()

        fun onMusicDetail(onlineMusic: OnlineMusic)

    }
    interface Presenter : BasePresenter<View>{

        fun loadMusicList(id : String)

        fun loadMusicDetail(onlineMusic: OnlineMusic)


    }

}
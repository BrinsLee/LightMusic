package com.brins.lightmusic.ui.fragment.usermusiclist

import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MusicListContract {
    interface View : BaseView{

        fun getLifeActivity(): AppCompatActivity


    }
    interface Presenter : BasePresenter<View>{

        suspend fun loadMusicList(id : String): MusicListDetailResult


    }

}
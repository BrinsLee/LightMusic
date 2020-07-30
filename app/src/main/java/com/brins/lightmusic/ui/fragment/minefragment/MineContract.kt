package com.brins.lightmusic.ui.fragment.minefragment

import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.model.userfm.UserFmResult
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MineContract {
    interface View : BaseView {

        fun getLifeActivity(): AppCompatActivity

        fun onUserProfileLoad()

        fun onFmLoad(result: UserFmResult)

        fun onUserMusicListLoad(result : UserPlayListResult)

        fun onLoadFail()

    }

    interface Presenter : BasePresenter<View> {
        /*
        * 获得用户歌单
        * */
        fun loadUserMusicList(id : String)

        /*
         * 更新用户歌单
         * */
        fun updateUserMusicList()
        /*
        * 获得用户电台
        * */
        fun loadUserFm()
        /*
        * 获得用户最近播放
        * */
        fun loadRecord()


    }
}
package com.brins.lightmusic.ui.fragment.mainfragment

import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.model.personal.PersonalizedResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

/**
 * @author lipeilin
 * @date 2020/7/22
 */
interface MainContract {

    interface View : BaseView {

        fun onPersonalizedMusicLoad(data: ArrayList<PersonalizedMusic>?)
    }

    interface Presenter : BasePresenter<View> {

        suspend fun loadPersonalizedMusic()

    }
}
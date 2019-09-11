package com.brins.lightmusic.ui.fragment.localmusic

import androidx.loader.app.LoaderManager
import com.brins.lightmusic.model.loaclmusic.LocalMusic
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface LocalMusicContract {

    interface View : BaseView<Presenter> {

        fun getLoaderManager() : LoaderManager

//        fun getcontext() : Context

        fun onLocalMusicLoaded(songs: MutableList<LocalMusic>)

    }

    interface Presenter : BasePresenter<View> {
        fun loadLocalMusic()
    }
}
package com.brins.lightmusic.ui.fragment.video

import android.content.Context
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvMetaData
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView
import io.reactivex.functions.Consumer

interface VideoContract {
    interface View : BaseView<Presenter>{
        fun showLoading()

        fun hideLoading()

        fun getcontext() : Context

        fun handleError(error: Throwable)

        fun onVideoLoad(videoLists : List<Mv>)

    }

    interface Presenter : BasePresenter {
        fun loadVideo()

        fun loadUrl(id : String,consumer: Consumer<MvMetaResult>)
    }
}
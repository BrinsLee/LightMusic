package com.brins.lightmusic.ui.fragment.video

import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvCommentsResult
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView
import io.reactivex.functions.Consumer

interface VideoContract {
    interface View : BaseView<Presenter> {
        fun onVideoLoad(videoLists: List<Mv>)

        fun onVideoCommomLoad(response: MvCommentsResult)
    }

    interface Presenter : BasePresenter<View> {
        fun loadVideo(limit: Int = 15, area: String)

        fun loadUrl(dataBean: LastestMvDataBean, consumer: Consumer<MvMetaResult>)

        fun loadVideoComments(id : String)
    }
}
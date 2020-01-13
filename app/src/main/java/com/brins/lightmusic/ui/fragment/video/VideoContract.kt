package com.brins.lightmusic.ui.fragment.video

import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvCommentsResult
import com.brins.lightmusic.model.musicvideo.MvMetaResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView
import io.reactivex.functions.Consumer
import retrofit2.Call

interface VideoContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
        suspend fun loadVideo(limit: Int = 15, area: String): List<Mv>

        suspend fun loadUrl(dataBean: LastestMvDataBean): MvMetaResult

        suspend fun loadVideoComments(id : String): MvCommentsResult
    }
}
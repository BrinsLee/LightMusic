package com.brins.lightmusic.ui.fragment.dailyrecommend

import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.model.dailyrecommend.DailyRecommendResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface DailyContract {

    interface View : BaseView {

        fun getLifeActivity(): AppCompatActivity

    }

    interface Presenter : BasePresenter<View> {
        suspend fun loadDailyRecommend() : DailyRecommendResult
    }
}
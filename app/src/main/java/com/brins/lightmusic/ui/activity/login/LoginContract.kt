package com.brins.lightmusic.ui.activity.login

import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface LoginContract {
    interface View :BaseView<Presenter>{

        fun handleError(error: Throwable)

        fun onLoginSuccess()

        fun onLoginFail()
    }
    interface Presenter : BasePresenter{
        fun startLogin()
    }
}
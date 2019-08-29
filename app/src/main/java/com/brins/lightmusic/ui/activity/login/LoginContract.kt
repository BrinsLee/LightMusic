package com.brins.lightmusic.ui.activity.login

import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface LoginContract {
    interface View :BaseView<Presenter>{

        fun handleError(error: Throwable)

        fun showLoading()

        fun hideLoading()

        fun onLoginSuccess(respone : UserLoginResult)

        fun onLoginFail()
    }
    interface Presenter : BasePresenter<View>{
        fun startLogin(request : UserLoginRequest)
    }
}
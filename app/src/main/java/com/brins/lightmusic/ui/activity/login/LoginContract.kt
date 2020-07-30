package com.brins.lightmusic.ui.activity.login

import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface LoginContract {
    interface View :BaseView{

        fun getLifeActivity(): AppCompatActivity


        fun onLoginSuccess(respone : UserLoginResult)

        fun onLoginFail()

        fun onLogoutSuccess()

        fun onLogoutFail()

        fun onCodeSendSuccess()

        fun onCodeSendFail(error : String)
    }
    interface Presenter : BasePresenter<View>{
        fun startLogin(request : UserLoginRequest)

        fun getCheckCode(phone : String)

        fun logout()
    }
}
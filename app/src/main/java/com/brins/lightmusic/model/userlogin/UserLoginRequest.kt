package com.brins.lightmusic.model.userlogin
import com.brins.lightmusic.ui.activity.login.LoginPresenter.Companion

class UserLoginRequest (var type : Companion.LOGIN_TYPE = Companion.LOGIN_TYPE.TYPE_EMAIL, var username : String = "", var password : String= "")
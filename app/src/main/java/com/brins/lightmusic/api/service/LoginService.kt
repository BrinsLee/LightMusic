package com.brins.lightmusic.api.service

import com.brins.lightmusic.common.AppConfig.LOGIN_EMAIL
import com.brins.lightmusic.common.AppConfig.LOGOUT
import com.brins.lightmusic.model.userlogin.LogoutResult
import com.brins.lightmusic.model.userlogin.UserLoginResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    /*
* 登录网络接口
* */
    @GET(LOGIN_EMAIL)
    fun Login_email(@Query("email")email : String, @Query("password")password :String): Observable<UserLoginResult>

    @GET(LOGOUT)
    fun Logout(): Observable<LogoutResult>
}
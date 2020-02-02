package com.brins.lightmusic.ui.activity.login

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.database.DatabaseFactory
import com.brins.lightmusic.model.userlogin.LogoutResult
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.utils.subscribeDbResult
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginPresenter @Inject constructor() : LoginContract.Presenter{


    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }

    val mLoginService = ApiHelper.getLoginService()

    private var mView: LoginContract.View? = null

    companion object {
        enum class LOGIN_TYPE {
            TYPE_WECHAT,
            TYPE_QQ,
            TYPE_PHONE,
            TYPE_EMAIL
        }
    }

    override fun getCheckCode(phone: String) {
        mLoginService.getCheckCode(phone).compose(AsyncTransformer<String>())
            .subscribe(object : DefaultObserver<String>(){
                override fun onSuccess(response: String) {
                    val s = response
                    Log.d("checkCode",s)
                    mView?.onCodeSendSuccess()
                }

                override fun onFail(message: String) {
                    mView?.onCodeSendFail(message)
                }

            })
    }

    override fun startLogin(request: UserLoginRequest) {
        mView?.showLoading()
        when (request.type) {
            LOGIN_TYPE.TYPE_EMAIL -> emailLogin(request)
            LOGIN_TYPE.TYPE_PHONE -> phoneLogin(request)
            LOGIN_TYPE.TYPE_QQ -> qqLogin(request)
            LOGIN_TYPE.TYPE_WECHAT -> wechatLogin(request)
        }

    }

    override fun logout() {
        mLoginService.logout().compose(AsyncTransformer<LogoutResult>())
            .subscribe(object : DefaultObserver<LogoutResult>() {
                override fun onSuccess(response: LogoutResult) {
                    if (response.code == 200) {
                        clearUserInfo()
                    }
                }

                override fun onFail(message: String) {
                    mView?.onLoginFail()
                }

            })
    }

    private fun wechatLogin(request: UserLoginRequest) {

    }

    private fun qqLogin(request: UserLoginRequest) {

    }

    private fun emailLogin(request: UserLoginRequest) {
        mLoginService.loginEmail(request.username, request.password)
            .delay(1, TimeUnit.SECONDS)
            .compose(AsyncTransformer<UserLoginResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<UserLoginResult>() {
                override fun onSuccess(response: UserLoginResult) {
                    storeUserInfo(response)
                }

                override fun onFail(message: String) {
                    mView?.onLoginFail()
                }
            })
    }

    private fun phoneLogin(request: UserLoginRequest) {
        mLoginService.loginCellphone(request.username, request.password)
            .delay(1, TimeUnit.SECONDS)
            .compose(AsyncTransformer<UserLoginResult>())
            .autoDisposable(provider)
            .subscribe(object : DefaultObserver<UserLoginResult>() {
                override fun onSuccess(response: UserLoginResult) {
                    storeUserInfo(response)
                }

                override fun onFail(message: String) {
                    mView?.onLoginFail()
                }
            })
    }

    private fun storeUserInfo(result: UserLoginResult) {
        DatabaseFactory.storeUserInfo(result.account)
            .subscribeDbResult({
                DatabaseFactory.storeUserProfile(result.profile).subscribeDbResult({
                    mView?.onLoginSuccess(result)
                }, {
                    mView?.onLoginFail()
                })
            }, {
                mView?.onLoginFail()
            })
    }

    private fun clearUserInfo() {
        DatabaseFactory.deleteUserInfo().subscribeDbResult({
            DatabaseFactory.deleteUserProfile().subscribeDbResult({
                mView?.onLogoutSuccess()
            }, {
            })

        }, {
            mView?.onLogoutFail()
        })
    }

    override fun subscribe(view: LoginContract.View?) {
        mView = view
    }

    override fun unsubscribe() {

        mView = null
    }

}
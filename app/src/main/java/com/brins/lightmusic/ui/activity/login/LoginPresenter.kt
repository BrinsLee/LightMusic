package com.brins.lightmusic.ui.activity.login

import androidx.lifecycle.Lifecycle
import com.brins.lightmusic.api.ApiHelper
import com.brins.lightmusic.api.DefaultObserver
import com.brins.lightmusic.common.AsyncTransformer
import com.brins.lightmusic.model.database.DatabaseFactory
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.utils.subscribeDbResult
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable

class LoginPresenter() : LoginContract.Presenter {

    val provider: AndroidLifecycleScopeProvider by lazy {
        AndroidLifecycleScopeProvider.from(mView?.getLifeActivity(), Lifecycle.Event.ON_DESTROY)
    }

    val mLoginService = ApiHelper.getLoginService()

    private var mView: LoginContract.View? = null

    companion object {
        val instance = SingletonHolder.holder

        enum class LOGIN_TYPE {
            TYPE_WECHAT,
            TYPE_QQ,
            TYPE_PHONE,
            TYPE_EMAIL
        }
    }

    private object SingletonHolder {
        val holder = LoginPresenter()
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

    private fun wechatLogin(request: UserLoginRequest) {

    }

    private fun qqLogin(request: UserLoginRequest) {

    }

    private fun emailLogin(request: UserLoginRequest) {
        mLoginService.Login_email(request.username, request.password)
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


    override fun subscribe(view: LoginContract.View?) {
        mView = view
        mView!!.setPresenter(this)
    }

    override fun unsubscribe() {

        mView = null
    }

}
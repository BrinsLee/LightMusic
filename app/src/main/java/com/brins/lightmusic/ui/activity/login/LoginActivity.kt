package com.brins.lightmusic.ui.activity.login

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.common.AppConfig.PASSWORD
import com.brins.lightmusic.common.AppConfig.USERNAME
import com.brins.lightmusic.model.userlogin.UserAccountBean
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.utils.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.View, View.OnClickListener {

    lateinit var mPresenter: LoginContract.Presenter

    companion object {
        fun startThisActivity(activity: AppCompatActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }


    override fun onClick(v: View?) {
        USERNAME = et_username.text.toString()
        PASSWORD = et_password.text.toString()
        if (USERNAME.isEmpty() || PASSWORD.isEmpty()) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        val request =
            UserLoginRequest(LoginPresenter.Companion.LOGIN_TYPE.TYPE_EMAIL, USERNAME, PASSWORD)
        mPresenter.startLogin(request)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        LoginPresenter.instance.subscribe(this)
        btn_login.setOnClickListener(this)
    }

    override fun showLoading() {
        val weight = btn_login.measuredWidth.toFloat()
        val height = btn_login.measuredHeight.toFloat()
        input_layout_name.visibility = View.INVISIBLE
        input_layout_psw.visibility = View.INVISIBLE
        val set = inputAnimator(input_layout, weight, height)
        set.start()
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                layout_progress.visibility = View.VISIBLE
                progressAnimator(layout_progress)
                input_layout.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}

        })
    }

    override fun hideLoading() {
        layout_progress.visibility = View.GONE
        input_layout.visibility = View.VISIBLE
        input_layout_name.visibility = View.VISIBLE
        input_layout_psw.visibility = View.VISIBLE
        recovery(input_layout)
    }

    override fun onCreateBeforeBinding(savedInstanceState: Bundle?) {
        super.onCreateBeforeBinding(savedInstanceState)

    }

    fun saveUserAccount(info: UserLoginResult) {
        AppConfig.userAccount = info.account
        AppConfig.userProfile = info.profile
        SpUtils.obtain(SP_USER_INFO, this).save(KEY_IS_LOGIN, true)
        AppConfig.isLogin = true
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }

    //MVP View
    override fun handleError(error: Throwable) {
    }

    override fun onLoginSuccess(respone: UserLoginResult) {
        saveUserAccount(respone)
        Log.d(TAG, "success :")
    }

    override fun onLoginFail() {
        hideLoading()
    }

    override fun setPresenter(presenter: LoginContract.Presenter?) {
        mPresenter = presenter!!

    }

    override fun getLifeActivity(): AppCompatActivity {
        return this
    }
}

package com.brins.lightmusic.ui.activity.login

import android.animation.Animator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.BuildConfig
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.common.AppConfig.*
import com.brins.lightmusic.model.userlogin.Item
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.ui.customview.TimeCountDown
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.canking.minipay.Config
import com.canking.minipay.MiniPayUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.avatar
import kotlinx.android.synthetic.main.activity_unlogin.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginContract.View, View.OnClickListener,
    OnItemClickListener, CommonHeaderView.OnBackClickListener {


    override fun initInject() {
        getActivityComponent().inject(this)

    }

    private lateinit var timeCountDown: TimeCountDown
    private var phoneNumber: String = ""
    @Inject
    lateinit var mPresenter: LoginPresenter

    private var isLogin = false
    private var mAvatar: Bitmap? = null
    private var mList: ArrayList<Item>? = null
    private var mAdapter: CommonViewAdapter<Item>? = null
    private var mLoginType = EMAIL_LOGIN
    private var canClickGetCode = true


    companion object {
        val IS_LOGIN = "isLogin"
        val LOGIN_SUCCESS_CODE = 1002
        val LOGIN_FAIL_CODE = 1001
        val LOGOUT_SUCCESS_CODE = 1003
        val EMAIL_LOGIN = 1004
        val PHONE_LOGIN = 1005


        fun startThisActivity(
            fragment: Fragment,
            login: Boolean
        ) {
            val intent = Intent(fragment.activity, LoginActivity::class.java)
            intent.putExtra(IS_LOGIN, login)
            fragment.startActivityForResult(intent, 1)
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                USERNAME = et_username.text.toString()
                PASSWORD = et_password.text.toString()
                if (USERNAME.isEmpty() || PASSWORD.isEmpty()) {
                    CustomToast.makeText(this, getString(R.string.username_not_empty), Toast.LENGTH_SHORT,CustomToast.TYPE_FAILURE)
                    return
                }
                if (mLoginType == PHONE_LOGIN && !sendCheckVerifyCode()) {
                    return
                }
                val request =
                    if (mLoginType == EMAIL_LOGIN)
                        UserLoginRequest(
                            LoginPresenter.Companion.LOGIN_TYPE.TYPE_EMAIL,
                            USERNAME,
                            PASSWORD
                        ) else
                        UserLoginRequest(
                            LoginPresenter.Companion.LOGIN_TYPE.TYPE_PHONE,
                            USERNAME,
                            PASSWORD
                        )

                (mPresenter as? LoginPresenter)?.startLogin(request)
            }
            R.id.logout -> {
                //todo 确定弹框
                (mPresenter as? LoginPresenter)?.logout()
            }
            R.id.login_type -> {
                switchLoginType()
            }
            R.id.getCode_tv -> {
                getCheckCode()
            }
        }
    }

    private fun getCheckCode() {
        if (!canClickGetCode) {
            return
        }
        sendCheckVerifyCode()
    }

    private fun switchLoginType() {
        when (mLoginType) {
            EMAIL_LOGIN -> {
                mLoginType = PHONE_LOGIN
                upper_iv.setImageResource(R.drawable.ic_phone)
                et_username.hint = getString(R.string.phone_num)
                login_type.text = getString(R.string.use_email)
            }
            PHONE_LOGIN -> {
                mLoginType = EMAIL_LOGIN
                upper_iv.setImageResource(R.drawable.ic_email)
                et_username.hint = getString(R.string.email)
                login_type.text = getString(R.string.use_phone)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return if (isLogin) R.layout.activity_login else R.layout.activity_unlogin
    }


    override fun onCreateBeforeBinding(savedInstanceState: Bundle?) {
        super.onCreateBeforeBinding(savedInstanceState)
        setTranslucent(this)
        isLogin = intent.getBooleanExtra(IS_LOGIN, false)
    }


    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        mPresenter.subscribe(this)
        setTranslucent(this)
        if (isLogin) {
            if (mAvatar == null) {
                Glide.with(this)
                    .load(AppConfig.userProfile.avatarUrl)
                    .into(avatar)
            } else {
                avatar.setImageBitmap(mAvatar)
            }
            nickName.text = AppConfig.userProfile.nickname
            initList()
            headLogin.setOnBackClickListener(this)
            logout.setOnClickListener(this)
        } else {
            getCode_tv.setOnClickListener(this)
            login_type.setOnClickListener(this)
            btn_login.setOnClickListener(this)
            headUnlogin.setOnBackClickListener(this)
            et_username.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (s.length == AppConfig.PHONE_NUMBER_LENGTH) {
                        getCode_tv.setTextColor(
                            ContextCompat.getColor(
                                this@LoginActivity,
                                R.color.blue
                            )
                        )
                        getCode_tv.isClickable = true
                    } else {
                        getCode_tv.setTextColor(
                            ContextCompat.getColor(
                                this@LoginActivity,
                                R.color.default_btn_text
                            )
                        )
                        getCode_tv.isClickable = false
                    }

                    if (s.length > 0) {
                        cancel_input_btn.visibility = View.VISIBLE
                    } else {
                        cancel_input_btn.visibility = View.GONE
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }
    }


    fun onCancelInput(view: View) {
        et_username.setText("")
        cancel_input_btn.setVisibility(View.GONE)
    }

    private fun initList() {
        mList = arrayListOf()
/*      mList!!.add(Item(TYPE_MESSAGE, "我的消息", R.drawable.ic_message))
        mList!!.add(Item(TYPE_FRIEND, "我的好友", R.drawable.ic_friends))
        mList!!.add(Item(TYPE_THEME, "更换主题", R.drawable.ic_theme))
        mList!!.add(Item(TYPE_DONATE, "捐赠", R.drawable.ic_donate))
*/
        mList!!.add(Item(TYPE_ABOUT, "版本：${BuildConfig.VERSION_NAME}", R.drawable.ic_about))
        mList!!.add(Item(TYPE_DONATE, "请作者喝咖啡", R.drawable.ic_donate))
        mAdapter = object :
            CommonViewAdapter<Item>(this@LoginActivity, R.layout.item_login_selector, mList!!) {
            override fun converted(holder: ViewHolder, t: Item, position: Int) {
                val playList = (list[position])
                holder.setImageResource(R.id.imgCover, playList.icon)
                holder.setText(R.id.textViewName, playList.name)

            }
        }
        mAdapter!!.setOnItemClickListener(this)
        itemRecycler.adapter = mAdapter
        itemRecycler.layoutManager = LinearLayoutManager(this)
        itemRecycler.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onBackClick(view: View) {
        finish()
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


    private fun saveUserAccount(info: UserLoginResult) {
        AppConfig.userAccount = info.account
        AppConfig.userProfile = info.profile
        SpUtils.obtain(SP_USER_INFO, this).save(KEY_IS_LOGIN, true)
        AppConfig.isLogin = true
        setResult(LOGIN_SUCCESS_CODE)
        finish()
    }

    private fun clearUserAccount() {
        AppConfig.userAccount = null
        AppConfig.userProfile = null
        SpUtils.obtain(SP_USER_INFO, this).save(KEY_IS_LOGIN, false)
        AppConfig.isLogin = false
        setResult(LOGOUT_SUCCESS_CODE)
        finish()
    }

    private fun sendCheckVerifyCode(): Boolean {
        phoneNumber = et_username.text.toString().trim { it <= ' ' }
        val match = isMobileSimple(phoneNumber)
        if (!match) {
            CustomToast.makeText(
                this@LoginActivity,
                getString(R.string.phone_format_err),
                Toast.LENGTH_SHORT,
                CustomToast.TYPE_FAILURE
            )
            return false
        }
        return true
/*
        timeCountDown = TimeCountDown(60 * 1000, 1000, getCode_tv, this@LoginActivity)
        mPresenter.getCheckCode(phoneNumber)
        timeCountDown.start()*/
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
        setResult(LOGIN_FAIL_CODE)
        finish()
    }

    override fun onLogoutSuccess() {
        clearUserAccount()
        Log.d(TAG, "logout success :")

    }

    override fun onLogoutFail() {
        CustomToast.makeText(this,"退出登录失败",Toast.LENGTH_SHORT,CustomToast.TYPE_FAILURE)
    }

    override fun onCodeSendSuccess() {
        CustomToast.makeText(
            this,
            R.string.get_code_success,
            Toast.LENGTH_SHORT,
            CustomToast.TYPE_SUCCESS
        )
    }

    override fun onCodeSendFail(error: String) {
        CustomToast.makeText(this, error, Toast.LENGTH_SHORT, CustomToast.TYPE_FAILURE)

    }

    override fun getLifeActivity(): AppCompatActivity {
        return this
    }


    override fun onItemClick(view: View?, position: Int) {
        when (position) {
            1 -> MiniPayUtils.setupPay(
                this,
                Config.Builder(ALIPAY, R.drawable.ic_ali_pay, R.drawable.ic_wechat_pay).build()
            )
        }
    }

}

package com.brins.lightmusic.ui.activity.login

import android.animation.Animator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.common.AppConfig.PASSWORD
import com.brins.lightmusic.common.AppConfig.USERNAME
import com.brins.lightmusic.model.userlogin.Item
import com.brins.lightmusic.model.userlogin.UserLoginRequest
import com.brins.lightmusic.model.userlogin.UserLoginResult
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.avatar
import kotlinx.android.synthetic.main.activity_unlogin.*

class LoginActivity : BaseActivity(), LoginContract.View, View.OnClickListener,
    OnItemClickListener {


    private lateinit var mPresenter: LoginContract.Presenter
    private var isLogin = false
    private var mAvatar: Bitmap? = null
    private var mList: ArrayList<Item>? = null
    private var mAdapter: CommonViewAdapter<Item>? = null


    companion object {
        val IS_LOGIN = "isLogin"
        val LOGIN_SUCCESS_CODE = 1002
        val LOGIN_FAIL_CODE = 1001

        fun startThisActivity(
            fragment: Fragment,
            login: Boolean
        ) {
            val intent = Intent(fragment.activity, LoginActivity::class.java)
            intent.putExtra(IS_LOGIN, login)
            fragment.startActivityForResult(intent, 1)
        }
    }


    override fun onClick(v: View?) {
        USERNAME = et_username.text.toString()
        PASSWORD = et_password.text.toString()
        if (USERNAME.isEmpty() || PASSWORD.isEmpty()) {
            Toast.makeText(this, getString(R.string.username_not_empty), Toast.LENGTH_SHORT).show()
            return
        }
        val request =
            UserLoginRequest(LoginPresenter.Companion.LOGIN_TYPE.TYPE_EMAIL, USERNAME, PASSWORD)
        mPresenter.startLogin(request)
    }

    override fun getLayoutResId(): Int {
        return if (isLogin) R.layout.activity_login else R.layout.activity_unlogin
    }


    override fun onCreateBeforeBinding(savedInstanceState: Bundle?) {
        super.onCreateBeforeBinding(savedInstanceState)
        isLogin = intent.getBooleanExtra(IS_LOGIN, false)
    }


    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        LoginPresenter.instance.subscribe(this)
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
        } else {
            btn_login.setOnClickListener(this)

        }
    }

    private fun initList() {
        mList = arrayListOf()
        mList!!.add(Item(TYPE_MESSAGE, "我的消息", R.drawable.ic_message))
        mList!!.add(Item(TYPE_FRIEND, "我的好友", R.drawable.ic_friends))
        mList!!.add(Item(TYPE_THEME, "更换主题", R.drawable.ic_theme))
        mList!!.add(Item(TYPE_DONATE, "捐赠", R.drawable.ic_donate))
        mList!!.add(Item(TYPE_ABOUT, "关于", R.drawable.ic_about))
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


    fun saveUserAccount(info: UserLoginResult) {
        AppConfig.userAccount = info.account
        AppConfig.userProfile = info.profile
        SpUtils.obtain(SP_USER_INFO, this).save(KEY_IS_LOGIN, true)
        AppConfig.isLogin = true
        setResult(LOGIN_SUCCESS_CODE)
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
        setResult(LOGIN_FAIL_CODE)
        finish()
    }


    override fun getLifeActivity(): AppCompatActivity {
        return this
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }

    override fun onItemClick(position: Int) {

    }

}

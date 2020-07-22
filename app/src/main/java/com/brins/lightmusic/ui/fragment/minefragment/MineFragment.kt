package com.brins.lightmusic.ui.fragment.minefragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.userfm.UserFmResult
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import com.brins.lightmusic.ui.activity.login.LoginActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CustomStaggeredGridLayoutManager
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.fragment.usermusiclist.UserPlayListActivity
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.item_grid_view.*
import javax.inject.Inject

class MineFragment : BaseFragment(), View.OnClickListener, OnItemClickListener, MineContract.View {

    private var mAvatar: Bitmap? = null

    @Inject
    lateinit var mPresenter: MinePresenter
    private lateinit var mAdapter: CommonViewAdapter<UserPlayListBean>
    private lateinit var customStaggeredGridLayoutManager: CustomStaggeredGridLayoutManager


    private var mPlayList: ArrayList<UserPlayListBean> = arrayListOf()

    override fun getLayoutResID(): Int {
        return R.layout.fragment_mine
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mPresenter.subscribe(this)
        setListener()
        checkToLoad()
    }

    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    override fun onCreateViewAfterBinding() {
        super.onCreateViewAfterBinding()
        setListener()
        initUserData()
    }

    private fun setListener() {
        login_tv.setOnClickListener(this)
    }

    private fun initUserData() {

        if (AppConfig.isLogin) {
            un_login_root.visibility = View.GONE
            login_root.visibility = View.VISIBLE
            if (AppConfig.userAccount != null && AppConfig.userProfile != null) {
                if (mAvatar == null) {
                    Glide.with(this)
                        .load(AppConfig.userProfile.avatarUrl)
                        .into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                val bitmapDrawable = resource as BitmapDrawable
                                mAvatar = bitmapDrawable.bitmap
                                avatar.setImageBitmap(mAvatar)
                                val a = getStringCover(mAvatar)
                                SpUtils.obtain(SP_USER_INFO, activity).save(KEY_AVATAR_STRING, a)
                            }
                        })
                } else {
                    avatar.setImageBitmap(mAvatar)
                }
                nickName.text = AppConfig.userProfile.nickname
                checkToLoad()
            }
        } else {
            un_login_root.visibility = View.VISIBLE
            login_root.visibility = View.GONE
        }
        date.text = getCalendarDay()
    }

    private fun checkToLoad() {
        if (mPlayList.isEmpty() && mPresenter.isSubscribe()) {
            mPresenter.loadUserMusicList(AppConfig.userAccount.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAvatar?.recycle()
    }

    private fun clearUserData() {
        avatar.setImageResource(R.drawable.ic_avatar)
        nickName.text = getString(R.string.login)
        if (mPlayList.isNotEmpty()) {
            mPlayList.clear()
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_tv -> LoginActivity.startThisActivity(this, AppConfig.isLogin)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("aaaa", "$resultCode")
        when (resultCode) {
            LoginActivity.LOGIN_SUCCESS_CODE -> {
                initUserData()
            }
            LoginActivity.LOGIN_FAIL_CODE -> {

            }
            LoginActivity.LOGOUT_SUCCESS_CODE -> {
                clearUserData()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onUserProfileLoad() {
        TODO("Not yet implemented")
    }

    override fun onFmLoad(result: UserFmResult) {
        TODO("Not yet implemented")
    }

    override fun onUserMusicListLoad(result: UserPlayListResult) {

        if (result.playlist != null) {
            mPlayList = result.playlist!!
            mAdapter = object :
                CommonViewAdapter<UserPlayListBean>(
                    activity!!,
                    R.layout.item_local_music,
                    mPlayList
                ) {
                override fun converted(holder: ViewHolder, t: UserPlayListBean, position: Int) {
                    val playlist = list[position]
                    val width = (ScreenUtils.getScreenWidth(context) - dp2px(context, 25f)) / 2
                    val layoutparams = holder.getView<ImageView>(R.id.imgCover).layoutParams
                    layoutparams.width = width
                    layoutparams.height = width
                    holder.getView<ImageView>(R.id.imgCover).layoutParams = layoutparams
                    ImageLoadreUtils.getInstance().loadImage(
                        context,
                        ImageLoader.Builder().url(playlist.coverImgUrl).assignWidth(width)
                            .assignHeight(
                                width
                            )
                            .scaleModeType(ImageLoadreUtils.SCALE_MODE_CENTER_CROP).imgView(
                                holder.getView(
                                    R.id.imgCover
                                )
                            ).bulid()
                    )
                    holder.setText(R.id.textViewName, playlist.name)
                    holder.setText(R.id.textViewArtist, "共${playlist.trackCount}首")
                }
            }
            customStaggeredGridLayoutManager =
                CustomStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            customStaggeredGridLayoutManager.setScrollEnabled(false)
            userPlayList.isNestedScrollingEnabled = false
            userPlayList.layoutManager = customStaggeredGridLayoutManager
            userPlayList.adapter = mAdapter
            mAdapter.setOnItemClickListener(this)
        }
    }

    override fun onLoadFail() {
        TODO("Not yet implemented")
    }

    override fun onItemClick(view: View?, position: Int) {
        view?.let {
            UserPlayListActivity.startThis(
                view.findViewById(R.id.imgCover) as ImageView,
                mPlayList[position],
                activity as Activity
            )
        }
        /*try {
            (activity as MainActivity).switchFragment(UserMusicListFragment(mPlayList[position]))
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }*/
    }
}
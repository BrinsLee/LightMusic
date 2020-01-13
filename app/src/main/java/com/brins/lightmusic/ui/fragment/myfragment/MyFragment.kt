package com.brins.lightmusic.ui.fragment.myfragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.userfm.UserFmResult
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.activity.login.LoginActivity
import com.brins.lightmusic.ui.activity.login.LoginActivity.Companion.LOGIN_FAIL_CODE
import com.brins.lightmusic.ui.activity.login.LoginActivity.Companion.LOGIN_SUCCESS_CODE
import com.brins.lightmusic.ui.activity.login.LoginActivity.Companion.LOGOUT_SUCCESS_CODE
import com.brins.lightmusic.ui.fragment.usermusiclist.UserMusicListFragment
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.fragment.dailyrecommend.DailyRecommendFragment
import com.brins.lightmusic.ui.fragment.localmusic.LocalMusicFragment
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_my.*
import kotlinx.android.synthetic.main.item_grid_view.*
import kotlinx.android.synthetic.main.item_recycler_head.*
import java.lang.Exception
import javax.inject.Inject

class MyFragment : BaseFragment(), MyContract.View, OnItemClickListener,
    View.OnClickListener {
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    @Inject
    lateinit var mPresenter: MyPresenter
    private lateinit var mAdapter: CommonViewAdapter<UserPlayListBean>
    private lateinit var mFmList: PlayList
    private var mPlayList: ArrayList<UserPlayListBean> = arrayListOf()
    private var mAvatar: Bitmap? = null
    private var isListOpen = false


    override fun getLayoutResID(): Int {
        return R.layout.fragment_my
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mPresenter.subscribe(this)
        setListener()
        initUserData()
    }


    private fun initUserData() {

        if (AppConfig.isLogin) {
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
        }
        date.text = getCalendarDay()
    }


    private fun setListener() {
        avatar.setOnClickListener(this)
        nickName.setOnClickListener(this)
        localMusic.setOnClickListener(this)
        fm.setOnClickListener(this)
        collection.setOnClickListener(this)
        rootli.setOnClickListener(this)

    }


    override fun onItemClick(position: Int) {
//        UserMusicListActivity.startThisActivity(activity as AppCompatActivity, mPlayList[position])
        /*try {
            (activity as MainActivity).switchFragment(UserMusicListFragment(mPlayList[position]))
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }*/
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.avatar, R.id.nickName -> LoginActivity.startThisActivity(this, AppConfig.isLogin)
            R.id.fm -> mPresenter.loadUserFm()
            R.id.collection -> switch(DailyRecommendFragment())
            R.id.localMusic -> switch(LocalMusicFragment())
            R.id.rootli -> openMusicList()
        }

    }

    private fun openMusicList() {
        isListOpen = !isListOpen
        if (isListOpen) {
            userPlayList.visibility = View.VISIBLE
            status.setImageResource(R.drawable.ic_chevron_down)
        } else {
            userPlayList.visibility = View.INVISIBLE
            status.setImageResource(R.drawable.ic_chevron_right)
        }
    }

    private fun switch(fragment: Fragment) {
/*        try {
            (activity as MainActivity).switchFragment(fragment)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        mAvatar?.recycle()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("aaaa", "$resultCode")
        when (resultCode) {
            LOGIN_SUCCESS_CODE -> {
                initUserData()
            }
            LOGIN_FAIL_CODE -> {

            }
            LOGOUT_SUCCESS_CODE -> {
                clearUserData()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun clearUserData() {
        avatar.setImageResource(R.drawable.ic_avatar)
        nickName.text = getString(R.string.login)
        if (mPlayList.isNotEmpty()) {
            mPlayList.clear()
            mAdapter.notifyDataSetChanged()
        }
    }

    //MVP View

    override fun onFmLoad(result: UserFmResult) {
        mFmList = PlayList()
        mFmList.addSong(result.fmList)
        mFmList.setPlayingIndex(0)
        RxBus.getInstance().post(PlayListEvent(mFmList, 0, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:", "UserMusicListActivity")
        hideLoading()
    }

    override fun onUserProfileLoad() {
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
                    holder.setImageResource(R.id.imgCover, playlist.coverImgUrl)
                    holder.setText(R.id.textViewName, playlist.name)
                    holder.setText(R.id.textViewArtist, "共${playlist.trackCount}首")
                }
            }
            userPlayList.adapter = mAdapter
            userPlayList.layoutManager = LinearLayoutManager(context)
            mAdapter.setOnItemClickListener(this)
        }
    }


    override fun onLoadFail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun checkToLoad() {
        if (mPlayList.isEmpty()) {
            mPresenter.loadUserMusicList(AppConfig.userAccount.id)
        }
    }

}

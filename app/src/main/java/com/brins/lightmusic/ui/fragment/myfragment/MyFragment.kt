package com.brins.lightmusic.ui.fragment.myfragment


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brins.lib_common.utils.SpUtils
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.model.userplaylist.UserPlayListResult
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.activity.usermusiclist.UserMusicListActivity
import com.brins.lightmusic.ui.activity.login.LoginActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.TreeRecyclerViewAdapter
import com.brins.lightmusic.ui.customview.DefaultAnimator
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.ui.fragment.localmusic.LocalMusicFragment
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_my.*
import kotlinx.android.synthetic.main.item_grid_view.*
import java.lang.Exception

class MyFragment : BaseFragment(),MyContract.View, OnItemClickListener, View.OnClickListener {


    lateinit var mAdapter: TreeRecyclerViewAdapter<UserPlayListBean>
    private lateinit var myPresenter : MyPresenter
    private var mPlayList : ArrayList<UserPlayListBean>? = null
    private var mAvatar: Bitmap? = null
    private val list = arrayListOf(UserPlayListBean())


    override fun getLayoutResID(): Int {
        return R.layout.fragment_my
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyPresenter.instance.subscribe(this)
        mAdapter = TreeRecyclerViewAdapter(list)
        setListener()
        userPlayList.setAdapter(mAdapter)
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)

    }

    override fun onResume() {
        super.onResume()
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
                if (mPlayList == null)myPresenter.loadUserMusicList(AppConfig.userAccount.id)
            }
        }
    }

    private fun setListener() {
        mAdapter.setOnItemClickListener(this)
        avatar.setOnClickListener(this)
        nickName.setOnClickListener(this)
        localMusic.setOnClickListener(this)

    }


    override fun onItemClick(position: Int) {
        UserMusicListActivity.startThisActivity(activity as AppCompatActivity,mPlayList!![position])
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.avatar, R.id.nickName -> LoginActivity.startThisActivity(activity as AppCompatActivity)
            R.id.localMusic -> try {
                (activity as MainActivity).switchFragment(LocalMusicFragment())
                    .addToBackStack(TAG)
                    .commit()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAvatar?.recycle()
    }

    class titleBean {

    }



    //MVP View
    override fun onUserProfileLoad() {
    }

    override fun onUserMusicListLoad(result: UserPlayListResult) {
        if(result.playlist != null){
            mPlayList = result.playlist
            mAdapter.setData(result.playlist!!)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onLoadFail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: MyContract.Presenter?) {
        myPresenter = presenter as MyPresenter
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }
}

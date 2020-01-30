package com.brins.lightmusic.ui.fragment.usermusiclist

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.ui.customview.CornersTransform
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_user_play_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserPlayListActivity : BaseActivity(), MusicListContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {
    companion object {
        private val USER_PLAY_LIST = "USER_LIST"

        fun startThis(view: View, mUserPlayList: UserPlayListBean, activity: Activity) {
            val intent = Intent(activity, UserPlayListActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(USER_PLAY_LIST, mUserPlayList)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    private var mScrollY = 0
    private var h = 0
    private lateinit var mUserPlayList: UserPlayListBean
    @Inject
    lateinit var mPresenter: MusicListPresenter
    private var mPlayList = PlayList()
    private var currentTime: Long = 0
    private lateinit var mAdapter: CommonViewAdapter<Music>
    override fun onItemClick(view: View?, position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        mPlayList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(mPlayList, position, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:", "UserMusicListActivity")
    }

    override fun onBackClick(view: View) {
        finish()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_user_play_list
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun handleError(error: Throwable) {
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        setTranslucent(this)
        mUserPlayList = intent.getParcelableExtra(USER_PLAY_LIST) as UserPlayListBean
        if (mPlayList.getNumOfSongs() == 0 && AppConfig.isLogin) {
            initView()
        }
    }

    private fun initView() {
        h = dp2px(this, 65f)
        mAdapter = object : CommonViewAdapter<Music>(
            this@UserPlayListActivity, R.layout.item_online_music,
            mPlayList.getSongs() as ArrayList<Music>
        ) {
            override fun converted(holder: ViewHolder, t: Music, position: Int) {
                val playlist = (list[position])
                holder.setText(R.id.name, playlist.name)
                holder.setText(R.id.artist, playlist.artistBeans!![0].name)
                ImageLoadreUtils.getInstance().loadImage(
                    this@UserPlayListActivity,
                    ImageLoader.Builder().url(t.album.picUrl).assignWidth(100).assignHeight(100).imgView(holder.getView(R.id.item_cover)).bitmapTransformation(
                        CornersTransform(20f)
                    ).bulid()
                )
            }

        }
        head.title = mUserPlayList.name
        ImageLoadreUtils.getInstance().loadImage(
            this,
            ImageLoader.Builder().url(mUserPlayList.coverImgUrl).bitmapTransformation(
                CornersTransform(10f)
            ).imgView(cover).bulid()
        )
        ImageLoadreUtils.getInstance().loadImage(
            this,
            ImageLoader.Builder().url(mUserPlayList.coverImgUrl).assignWidth(500).assignHeight(500).bitmapTransformation(
                CornersTransform(20f)
            ).bulid(), object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val result = rsBlur(this@UserPlayListActivity, resource, 25f)
                    coverBg.setImageBitmap(result)
                }
            }
        )
        nickName.text = mUserPlayList.creator.nickName
        Glide.with(this).load(mUserPlayList.creator.avatarUrl).into(avatar)
//        mPresenter.loadMusicList(mUserPlayList.id)
        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SpacesItemDecoration(this, 2, R.color.gery))
        recyclerView.adapter = mAdapter
        loadUserPlayList()
        setListener()
    }

    fun setListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                mScrollY += scrollY - oldScrollY
                if (mScrollY <= 0) {
                    head.setBackgroundResource(android.R.color.transparent)
                    //                tv_title.setText("")
                } else if (mScrollY in 1 until h) {
                    val fraction = mScrollY * 1f / h
                    head.setBackgroundColor(
                        changeAlpha(
                            ContextCompat.getColor(
                                this@UserPlayListActivity,
                                R.color.colorPrimary
                            ), fraction
                        )
                    )
                } else if (mScrollY >= h) {
                    head.setBackgroundResource(R.color.colorPrimary)
                }
            }
        }
        mAdapter.setOnItemClickListener(this)
        head.setOnBackClickListener(this)
    }


    fun changeAlpha(color: Int, fraction: Float): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, red, green, blue)
    }

    private fun loadUserPlayList() {
        launch({
            showLoading()
            val data = getUserPlayList()
            mPlayList.addSong(data.playlist!!.tracks)
            mAdapter.notifyDataSetChanged()
            hideLoading()
        }, {
            Toast.makeText(this, getString(R.string.login_first), Toast.LENGTH_SHORT).show()
            hideLoading()
        })
    }

    private suspend fun getUserPlayList() = withContext(Dispatchers.IO) {
        val result = mPresenter.loadMusicList(mUserPlayList.id)
        result
    }
}

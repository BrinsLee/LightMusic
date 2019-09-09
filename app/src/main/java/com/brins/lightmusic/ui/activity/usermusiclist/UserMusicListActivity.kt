package com.brins.lightmusic.ui.activity.usermusiclist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.TreeRecyclerViewAdapter
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_music_list.*
import java.util.ArrayList

class UserMusicListActivity : BaseActivity(), MusicListContract.View, OnItemClickListener,
    CommonHeaderView.OnBackClickListener {

    private var mUserPlayList: UserPlayListBean? = null
    private lateinit var mPresenter: MusicListContract.Presenter
    private var mPlayList = PlayList()

    private var mAdapter: TreeRecyclerViewAdapter<Music> =
        TreeRecyclerViewAdapter(mPlayList.getSongs() as ArrayList<Music>)


    override fun getLayoutResId(): Int {
        return R.layout.activity_user_music_list
    }

    companion object {
        val PLAYBEAN = "playlist_bean"
        fun startThisActivity(activity: AppCompatActivity, playlist: UserPlayListBean?) {
            val intent = Intent(activity, UserMusicListActivity::class.java)
            intent.putExtra(PLAYBEAN, playlist)
            activity.startActivity(intent)
        }
    }

    override fun onCreateBeforeBinding(savedInstanceState: Bundle?) {
        super.onCreateBeforeBinding(savedInstanceState)
        mUserPlayList = intent.getParcelableExtra(PLAYBEAN)
        MusicListPresenter.instance.subscribe(this)

    }

    fun setListener() {
        mAdapter.setOnItemClickListener(this)
        head.setOnBackClickListener(this)
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        if (mUserPlayList != null) {
            head.title = mUserPlayList!!.name
            Glide.with(this).load(mUserPlayList!!.coverImgUrl).into(cover)
            nickName.text = mUserPlayList!!.creator.nickName
            Glide.with(this).load(mUserPlayList!!.creator.avatarUrl).into(avatar)
            mPresenter.loadMusicList(mUserPlayList!!.id)
        }

        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        recyclerView.adapter = mAdapter
        setListener()

    }

    override fun onStart() {
        super.onStart()
        showBottomBar(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        removeBottomBar(supportFragmentManager)
    }

    override fun onItemClick(position: Int) {
        mPlayList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(mPlayList, position, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:","UserMusicListActivity")

    }

    override fun onBackClick(view: View) {
        finish()
    }

    //MVP View
    override fun handleError(error: Throwable) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onMusicListLoad(detailBean: MusicListDetailBean) {
        mPlayList.addSong(detailBean.tracks!!)

        mAdapter.notifyDataSetChanged()
    }

    override fun onLoadFail() {
    }

    override fun setPresenter(presenter: MusicListContract.Presenter) {
        mPresenter = presenter
    }

    override fun getLifeActivity(): AppCompatActivity {
        return this
    }

}

package com.brins.lightmusic.ui.activity.usermusiclist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.TreeRecyclerViewAdapter
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_music_list.*

class UserMusicListActivity : BaseActivity(), MusicListContract.View {


    private var mPlayList: UserPlayListBean? = null
    private var mPresenter: MusicListContract.Presenter? = null
    private var mMusicLists = arrayListOf<OnlineMusic>()
    private var mAdapter: TreeRecyclerViewAdapter<OnlineMusic> =
        TreeRecyclerViewAdapter(mMusicLists)


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
        mPlayList = intent.getParcelableExtra(PLAYBEAN)
        MusicListPresenter.instance.subscribe(this)
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        if (mPlayList != null) {
            head.title = mPlayList!!.name
            Glide.with(this).load(mPlayList!!.coverImgUrl).into(cover)
            nickName.text = mPlayList!!.creator.nickName
            Glide.with(this).load(mPlayList!!.creator.avatarUrl).into(avatar)
            mPresenter!!.loadMusicList(mPlayList!!.id)
        }

        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        recyclerView.adapter = mAdapter
    }

    //MVP View
    override fun handleError(error: Throwable) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onMusicListLoad(detailBean: MusicListDetailBean) {
        mMusicLists.addAll(detailBean.tracks!!)
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

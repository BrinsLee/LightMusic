package com.brins.lightmusic.ui.activity.usermusiclist


import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.TreeRecyclerViewAdapter
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user_music_list.*
import java.util.ArrayList

class UserMusicListFragment(var mUserPlayList: UserPlayListBean) : BaseFragment(),
    MusicListContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener
/*SwipeRefreshLayout.OnRefreshListener */ {


    private var mPlayList = PlayList()
    private var currentTime: Long = 0
    private lateinit var mPresenter: MusicListContract.Presenter
    private var mAdapter: TreeRecyclerViewAdapter<Music> =
        TreeRecyclerViewAdapter(mPlayList.getSongs() as ArrayList<Music>)

    override fun getLayoutResID(): Int {
        return R.layout.fragment_user_music_list
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        MusicListPresenter.instance.subscribe(this)
    }

    override fun onStart() {
        super.onStart()

        head.title = mUserPlayList.name
        Glide.with(this).load(mUserPlayList.coverImgUrl).into(cover)
        nickName.text = mUserPlayList.creator.nickName
        Glide.with(this).load(mUserPlayList.creator.avatarUrl).into(avatar)
        mPresenter.loadMusicList(mUserPlayList.id)
        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        recyclerView.adapter = mAdapter
        setListener()

    }

    fun setListener() {
        mAdapter.setOnItemClickListener(this)
        head.setOnBackClickListener(this)
    }

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
        return activity as AppCompatActivity
    }


    override fun onItemClick(position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        mPlayList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(mPlayList, position, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:","UserMusicListActivity")
    }

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()

    }

}

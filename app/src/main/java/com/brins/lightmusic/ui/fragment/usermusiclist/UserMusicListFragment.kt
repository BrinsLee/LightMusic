package com.brins.lightmusic.ui.fragment.usermusiclist


import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.launch
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user_music_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class UserMusicListFragment(var mUserPlayList: UserPlayListBean) :
    BaseFragment<MusicListContract.Presenter>(),
    MusicListContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener
/*SwipeRefreshLayout.OnRefreshListener */ {


    private var mPlayList = PlayList()
    private var currentTime: Long = 0
    private lateinit var mPresenter: MusicListContract.Presenter
    private lateinit var mAdapter: CommonViewAdapter<Music>

    override fun getLayoutResID(): Int {
        return R.layout.fragment_user_music_list
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        MusicListPresenter.instance.subscribe(this)
        if (mPlayList.getNumOfSongs() == 0 && AppConfig.isLogin){
            initView()
        }
    }

    private fun initView(){

        mAdapter = object : CommonViewAdapter<Music>(
            activity!!, R.layout.item_local_music,
            mPlayList.getSongs() as ArrayList<Music>
        ) {
            override fun converted(holder: ViewHolder, t: Music, position: Int) {
                val playlist = (list[position])
                holder.setImageResource(R.id.imgCover, playlist.album.picUrl)
                holder.setText(R.id.textViewName, playlist.name)
                holder.setText(R.id.textViewArtist, playlist.artistBeans!![0].name)
            }

        }
        head.title = mUserPlayList.name
        Glide.with(this).load(mUserPlayList.coverImgUrl).into(cover)
        nickName.text = mUserPlayList.creator.nickName
        Glide.with(this).load(mUserPlayList.creator.avatarUrl).into(avatar)
//        mPresenter.loadMusicList(mUserPlayList.id)
        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        recyclerView.adapter = mAdapter
        loadUserPlayList()
        setListener()
    }

    fun setListener() {
        mAdapter.setOnItemClickListener(this)
        head.setOnBackClickListener(this)
    }

    private fun loadUserPlayList(){
        launch({
            showLoading()
            val data = getUserPlayList()
            mPlayList.addSong(data.playlist!!.tracks)
            mAdapter.notifyDataSetChanged()
            hideLoading()
        },{
            Toast.makeText(context, getString(R.string.login_first), Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    private suspend fun getUserPlayList() = withContext(Dispatchers.IO){
        val result = mPresenter.loadMusicList(mUserPlayList.id)
        result
    }
    override fun showRetryView() {
        super.showRetryView()
    }

    /*    override fun onMusicListLoad(detailBean: MusicListDetailBean) {
        mPlayList.addSong(detailBean.tracks!!)
        mAdapter.notifyDataSetChanged()
    }*/


    override fun setPresenter(presenter: MusicListContract.Presenter) {
        mPresenter = presenter
    }

    override fun onItemClick(position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        mPlayList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(mPlayList, position, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:", "UserMusicListActivity")
    }

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }
}

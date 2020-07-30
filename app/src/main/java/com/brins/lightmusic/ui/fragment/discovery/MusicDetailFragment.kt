package com.brins.lightmusic.ui.fragment.discovery


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_music_detail.*
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.widget.CommonHeaderView
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MusicDetailFragment : BaseFragment(), DiscoveryContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {

    @Inject
    lateinit var mPresenter: DiscoverPresenter
    var id: String = ""
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()
    private var currentTime: Long = 0

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MusicDetailAdapter(playList.getSongs())
        mAdapter.animationEnable = true
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        nestScrollView.fadingView = toolbar
        nestScrollView.fadingHeightView = coverMusicList
        DiscoverPresenter.instance.subscribe(this)
        if (arguments != null) {
            id = arguments!!.getString("DiscoveryFragment", "")
            if (id === "") {
                id = arguments!!.getString("ArtistTabFragment", "")
                loadAlbumDetail()
            } else {
                loadMusicListDetail()
            }
            musicRecycler.adapter = mAdapter
            musicRecycler.layoutManager = LinearLayoutManager(context)
            musicRecycler.addItemDecoration(
                DividerItemDecoration(
                    context!!, LinearLayoutManager.VERTICAL
                )
            )
        }

    }

    private fun loadMusicListDetail() {
        launch({
            showLoading()
            val musicData = getMusicListData()
            val detailBean = musicData.playlist
            if (detailBean != null) {
                Glide.with(context!!)
                    .load(detailBean.coverImgUrl)
                    .into(coverMusicList)
                toolbar.title = detailBean.name
                playList.addSong(detailBean.tracks!!)
                mAdapter.setNewData(playList.getSongs())
                hideLoading()
            }

        }, {})
    }

    private fun loadAlbumDetail() {
        launch({
            showLoading()
            val musics = getAlbumData()
            Glide.with(context!!)
                .load(musics.album!!.picUrl)
                .into(coverMusicList)
            toolbar.title = musics.album!!.name
            playList.addSong(musics.songs!!)
            mAdapter.setNewData(playList.getSongs())
            hideLoading()
        }, {
            val i = it
            Toast.makeText(context,i.message,Toast.LENGTH_SHORT).show()
            hideLoading()

        })
    }


    private suspend fun getMusicListData() = withContext(Dispatchers.IO) {
        val musicList = mPresenter.loadMusicListDetail(id)
        musicList
    }

    private suspend fun getAlbumData() = withContext(Dispatchers.IO) {
        val albumList = mPresenter.loadAlbumDetail(id)
        albumList
    }

    //ItemClick
    override fun onItemClick(view: View?,position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        playList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
    }

    override fun onBackClick(view: View) {
        activity!!.onBackPressed()
    }

}

package com.brins.lightmusic.ui.fragment.discovery


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_music_detail.*
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.album.AlbumResult
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.ui.customview.LoadingFragment
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC


class MusicDetailFragment : BaseFragment<DiscoveryContract.Presenter>(), DiscoveryContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {


    private lateinit var mPresenter: DiscoveryContract.Presenter
    var id: String = ""
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()
    private var currentTime: Long = 0

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MusicDetailAdapter(context!!, playList.getSongs())
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        nestScrollView.fadingView = toolbar
        nestScrollView.fadingHeightView = coverMusicList
        DiscoverPresenter.instance.subscribe(this)
        if (arguments != null){
            id = arguments!!.getString("DiscoveryFragment","")
            if (id === ""){
                id = arguments!!.getString("ArtistTabFragment","")
                mPresenter.loadAlbumDetail(id)

            }else{
                mPresenter.loadMusicListDetail(id)
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

    //ItemClick
    override fun onItemClick(position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        playList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
    }

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()
    }


    //MVP VIEW

    override fun onAlbumDetailLoad(musics: AlbumResult) {
        Glide.with(context!!)
            .load(musics.album!!.picUrl)
            .into(coverMusicList)
        toolbar.title = musics.album!!.name
        playList.addSong(musics.songs!!)
        mAdapter.setData(playList.getSongs())
        mAdapter.notifyDataSetChanged()


    }

    override fun onMusicListLoad(songs: ArrayList<MusicListBean>, type: Int) {
    }

    override fun onBannerLoad(banners: ArrayList<Banner>) {
    }

    override fun onDetailLoad(detailBean: MusicListDetailBean) {
        Glide.with(context!!)
            .load(detailBean.coverImgUrl)
            .into(coverMusicList)
        toolbar.title = detailBean.name
        playList.addSong(detailBean.tracks!!)
        mAdapter.setData(playList.getSongs())
        mAdapter.notifyDataSetChanged()
    }

    override fun onMusicDetail(onlineMusic: Music) {
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter) {
        mPresenter = presenter
    }
}

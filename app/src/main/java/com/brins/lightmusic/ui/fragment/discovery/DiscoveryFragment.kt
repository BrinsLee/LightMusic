package com.brins.lightmusic.ui.fragment.discovery


import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.customview.LoadingFragment
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryContract.Companion.TYPE_HIGHT
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryContract.Companion.TYPE_HOT
import kotlinx.android.synthetic.main.fragment_discovery.*

class DiscoveryFragment : BaseFragment<DiscoveryContract.Presenter>(), DiscoveryContract.View,
    SwipeRefreshLayout.OnRefreshListener, DiscoveryAdapter.OnItemClickListener {



    private var isFresh: Boolean = false
    private var count: Int = 1
    private lateinit var mPresenter: DiscoveryContract.Presenter
    private lateinit var bannerList: ArrayList<Banner>
    private lateinit var musicListBean: ArrayList<MusicListBean>
    private lateinit var musicHotListBean: ArrayList<MusicListBean>
    private val bannerAdapter by lazy {
        DiscoveryAdapter(DiscoveryAdapter.TYPE_BANNER, bannerList)
    }
    private val musicListAdapter by lazy {
        DiscoveryAdapter(DiscoveryAdapter.TYPE_MUSIC_LIST, musicListBean)
    }

    private val musicHotAdapter by lazy {
        DiscoveryAdapter(DiscoveryAdapter.TYPE_MUSIC_LIST, musicHotListBean)

    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        getBanner()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getBanner() {
        DiscoverPresenter.instance.subscribe(this@DiscoveryFragment)
        DiscoverPresenter.instance.initDiscoveryView()
        initLoadingMore()
    }

    //MVP View

    override fun onDetailLoad(detailBean: MusicListDetailBean) {

    }


    override fun onMusicListLoad(songs: ArrayList<MusicListBean>, type: Int) {
        isFresh = false
        when (type) {
            TYPE_HOT -> {
                if (songs.size > 6) {
                    musicHotListBean.clear()
                    musicHotListBean.addAll(songs.subList(songs.size - 6, songs.size))
                } else {
                    musicHotListBean = songs
                }
                initHotMusicList()
            }
            TYPE_HIGHT -> {
                if (songs.size > 6) {
                    musicListBean.clear()
                    musicListBean.addAll(songs.subList(songs.size - 6, songs.size))
                } else {
                    musicListBean = songs
                }
                initMusicList()
            }
        }

    }

    private fun initHotMusicList() {
        musicHotAdapter.setOnItemClickListener(this)
        recycleMusiclist2.adapter = musicHotAdapter
        recycleMusiclist2.layoutManager = GridLayoutManager(context!!, 3)
    }

    override fun onBannerLoad(banners: ArrayList<Banner>) {
        bannerList = banners
        initBannerView()
    }

    override fun onMusicDetail(onlineMusic: Music) {
    }

    private fun initBannerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerBanner.layoutManager = layoutManager
        recyclerBanner.adapter = bannerAdapter
    }

    private fun initMusicList() {

        musicListAdapter.setOnItemClickListener(this)
        recycleMusiclist.adapter = musicListAdapter
        recycleMusiclist.layoutManager = GridLayoutManager(context!!, 3)

    }

    private fun initLoadingMore() {
        loadingMore.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)
        loadingMore.setDistanceToTriggerSync(700)
        loadingMore.setProgressBackgroundColorSchemeColor(Color.WHITE)
        loadingMore.setSize(SwipeRefreshLayout.DEFAULT)
        loadingMore.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        if (!isFresh) {
            ++count
            isFresh = true
            loadingMore.isRefreshing = false
            showLoading()
            mPresenter.loadMusicList(count * 6)
        }
    }


    override fun getLayoutResID(): Int {
        return R.layout.fragment_discovery
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter) {
        mPresenter = presenter
    }

    override fun onItemClick(view: View, position: Int) {
        val id = musicListBean[position].id
        try {
            (activity as MainActivity).switchFragment(id, MusicDetailFragment())
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }
}

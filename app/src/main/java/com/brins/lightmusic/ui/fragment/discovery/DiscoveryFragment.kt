package com.brins.lightmusic.ui.fragment.discovery


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_discovery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
@AndroidEntryPoint
class DiscoveryFragment : BaseFragment(), DiscoveryContract.View,
    SwipeRefreshLayout.OnRefreshListener, DiscoveryAdapter.OnItemClickListener {

    @Inject
    lateinit var mPresenter: DiscoverPresenter
    private var isFresh: Boolean = false
    private var count: Int = 1
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

    private fun getBanner() {
        DiscoverPresenter.instance.subscribe(this@DiscoveryFragment)
        showLoading()
        initLoadingMore()
        initBannerList()
        initMusicList()
        initHotMusicList()
    }

    /*
    * 获取横幅广告
    * */
    private fun initBannerList() {
        launch({
            bannerList = getBannerData().bannners!!
            initBannerView()
        }, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private suspend fun getBannerData() = withContext(Dispatchers.IO) {
        val banner = mPresenter.loadBanner()
        banner
    }

    private fun initBannerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerBanner.layoutManager = layoutManager
        LinearSnapHelper().attachToRecyclerView(recyclerBanner)
        recyclerBanner.adapter = bannerAdapter
    }

    /*
    * 获取热门歌单
    * */
    private fun initHotMusicList(limit: Int = 6) {
        launch({
            val musicHotList = getHotMusicList(limit).playlists as ArrayList<MusicListBean>
            if (musicHotList.size > 6) {
                musicHotListBean.clear()
                musicHotListBean.addAll(
                    musicHotList.subList(
                        musicHotList.size - 6,
                        musicHotList.size
                    )
                )
            } else {
                musicHotListBean = musicHotList
            }
            initHotMusicView()
        }, {
            if (context != null)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

        })
    }

    private suspend fun getHotMusicList(limit: Int) = withContext(Dispatchers.IO) {
        val musiclist = mPresenter.loadHotMusicList(limit)
        musiclist
    }

    private fun initHotMusicView() {
        musicHotAdapter.setOnItemClickListener(this)
        recycleMusiclist2.adapter = musicHotAdapter
        val manager = LinearLayoutManager(context!!)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        recycleMusiclist2.layoutManager = manager
        hideLoading()
    }

    /*
    * 获取歌单
    * */
    private fun initMusicList(limit: Int = 6) {
        launch({
            val musicList = getMusicList(limit).playlists as ArrayList<MusicListBean>
            if (musicList.size > 6) {
                musicListBean.clear()
                musicListBean.addAll(musicList.subList(musicList.size - 6, musicList.size))
            } else {
                musicListBean = musicList
            }
            initMusicView()
        }, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }


    private suspend fun getMusicList(limit: Int) = withContext(Dispatchers.IO) {
        val musicList = mPresenter.loadMusicList(limit)
        musicList
    }


    private fun initMusicView() {
        musicListAdapter.setOnItemClickListener(this)
        recycleMusiclist.adapter = musicListAdapter
        val manager = LinearLayoutManager(context!!)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        recycleMusiclist.layoutManager = manager
        hideLoading()
    }


    private fun initLoadingMore() {
        if (loadingMore == null) {
            return
        }
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
            initMusicList(count * 6)
            initHotMusicList(count * 6)
        }
    }


    override fun getLayoutResID(): Int {
        return R.layout.fragment_discovery
    }


    override fun onItemClick(view: View, id: String) {
        MusicDetailActivity.startThis((activity as AppCompatActivity), id)
    }

}

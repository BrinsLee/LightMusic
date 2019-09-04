package com.brins.lightmusic.ui.fragment.discovery


import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_discovery.*

class DiscoveryFragment : BaseFragment(), DiscoveryContract.View,
    SwipeRefreshLayout.OnRefreshListener {
    private var isFresh: Boolean = false
    private var count: Int = 1
    private lateinit var mPresenter: DiscoveryContract.Presenter
    private lateinit var bannerList: ArrayList<Banner>
    private lateinit var musicListBean: ArrayList<MusicListBean>
    private val bannerAdapter by lazy {
        DiscoveryAdapter(DiscoveryAdapter.TYPE_BANNER, bannerList)
    }
    private val musicListAdapter by lazy {
        DiscoveryAdapter(DiscoveryAdapter.TYPE_MUSIC_LIST, musicListBean)
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        getBanner()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getBanner() {
        DiscoverPresent.instance.subscribe(this@DiscoveryFragment)
        initLoadingMore()
    }

    //MVP View

    override fun onDetailLoad(detailBean: MusicListDetailBean) {

    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

/*    override fun getcontext(): Context {
        return context!!
    }*/


    override fun handleError(error: Throwable) {

    }

    override fun onMusicListLoad(songs: ArrayList<MusicListBean>) {
        isFresh = false
        songs.reverse()
        musicListBean = songs
        initMusicList()
    }

    override fun onBannerLoad(banners: ArrayList<Banner>) {
        bannerList = banners
        initBannerView()
    }

    override fun onMusicDetail(onlineMusic: OnlineMusic) {}

    private fun initBannerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerBanner.layoutManager = layoutManager
        recyclerBanner.adapter = bannerAdapter

        /*pileLayout.visibility = View.VISIBLE
        pileLayout.setAdapter(object : PileLayout.Adapter() {
            override fun getLayoutId(): Int {
                return R.layout.artist_item
            }

            override fun getItemCount(): Int {
                return bannerList.size
            }

            override fun bindView(view: View, index: Int) {
                var viewHolder: ViewHolder? = view.tag as ViewHolder?
                if (viewHolder == null) {
                    viewHolder = ViewHolder()
                    viewHolder.imageView = view.findViewById(R.id.iv_recovery)
                    viewHolder.textView = view.findViewById(R.id.introduce)
                    view.tag = viewHolder
                }
                viewHolder.textView!!.text = bannerList[index].name
                Glide.with(this@DiscoveryFragment)
                    .load(bannerList[index].picUrl)
                    .into(viewHolder.imageView!!)
            }
        })*/

    }

    private fun initMusicList() {

        musicListAdapter.setOnItemClickListener(object : DiscoveryAdapter.OnItemClickListener {
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

        })
        recycleMusiclist.adapter = musicListAdapter
        recycleMusiclist.layoutManager = GridLayoutManager(context!!,3)
/*        recycleMusiclist2.adapter = musicListAdapter
        recycleMusiclist2.layoutManager = GridLayoutManager(context!!,3)*/

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
//            mPresenter.loadMusicList(count * 12)
        }
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
        mPresenter = presenter!!
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_discovery
    }

    class ViewHolder {
        var imageView: ImageView? = null
        var textView: TextView? = null
    }

}

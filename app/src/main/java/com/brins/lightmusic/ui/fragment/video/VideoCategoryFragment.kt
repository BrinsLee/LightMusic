package com.brins.lightmusic.ui.fragment.video



import android.graphics.Color
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_video.*

class VideoCategoryFragment : BaseFragment<VideoContract.Presenter>(),VideoContract.View,
    OnItemClickListener, SwipeRefreshLayout.OnRefreshListener  {

    var count: Int = 1
    var videoList = mutableListOf<Mv>()
    val videoAdapter: VideoListAdapter by lazy { VideoListAdapter(videoList, context!!) }
    private var isFresh: Boolean = false
    override fun getLayoutResID(): Int {
        return R.layout.fragment_video_category
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        initView()
        initLoadingMore()

    }

    override fun setPresenter(presenter: VideoContract.Presenter) {
        mPresenter = presenter
    }

    private var mPresenter: VideoContract.Presenter? = null


    override fun onVideoLoad(videoLists: List<Mv>) {
        if (isFresh) isFresh = false
        videoList.addAll(videoLists)
//        videoAdapter.addData(videoList)
        videoAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
    }

    override fun onRefresh() {
        if (!isFresh) {
            ++count
            isFresh = true
            loadMore.isRefreshing = false
            showLoading()
            mPresenter?.loadVideo(count * 15)
        }
    }


    private fun initView() {
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    private fun initLoadingMore() {
        loadMore.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)
        loadMore.setDistanceToTriggerSync(700)
        loadMore.setProgressBackgroundColorSchemeColor(Color.WHITE)
        loadMore.setSize(SwipeRefreshLayout.DEFAULT)
        loadMore.setOnRefreshListener(this)
    }
}

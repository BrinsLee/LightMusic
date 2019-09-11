package com.brins.lightmusic.ui.fragment.video

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.JZVideoPalyerView
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment<VideoContract.Presenter>(), VideoContract.View,
    OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private var mPresenter: VideoContract.Presenter? = null
    private var isFresh: Boolean = false
    var count: Int = 1
    var videoList = mutableListOf<Mv>()
    val videoAdapter: VideoListAdapter by lazy { VideoListAdapter(videoList, context!!) }
    override fun getLayoutResID(): Int {
        return R.layout.fragment_video
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        initView()
        initLoadingMore()

    }

    private fun initView() {
        VideoPresent(this).subscribe(this)
        videoAdapter.setOnItemListener(this)
        videoListView.layoutManager = LinearLayoutManager(context)
        videoListView.adapter = videoAdapter
        videoListView.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {

            }

            override fun onChildViewDetachedFromWindow(view: View) {
                val jz = view.findViewById<JZVideoPalyerView>(R.id.video_player)
                if (jz?.jzDataSource != null && Jzvd.CURRENT_JZVD != null && jz.jzDataSource.containsTheUrl(
                        Jzvd.CURRENT_JZVD.jzDataSource.currentUrl
                    )
                ) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos()
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


    //ItemClick
    override fun onItemClick(position: Int) {

    }

    //MVP View


    override fun onVideoLoad(videoLists: List<Mv>) {
        if (isFresh) isFresh = false
        videoList.addAll(videoLists)
//        videoAdapter.addData(videoList)
        videoAdapter.notifyDataSetChanged()
    }

    private fun initLoadingMore() {
        loadMore.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)
        loadMore.setDistanceToTriggerSync(700)
        loadMore.setProgressBackgroundColorSchemeColor(Color.WHITE)
        loadMore.setSize(SwipeRefreshLayout.DEFAULT)
        loadMore.setOnRefreshListener(this)
    }

    override fun setPresenter(presenter: VideoContract.Presenter) {
        mPresenter = presenter

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
}

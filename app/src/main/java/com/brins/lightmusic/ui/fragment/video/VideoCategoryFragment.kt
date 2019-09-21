package com.brins.lightmusic.ui.fragment.video


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvCommentsResult
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_video_category.*

class VideoCategoryFragment(var area: String) : BaseFragment<VideoContract.Presenter>(),
    VideoContract.View,
    OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private var mPresenter: VideoContract.Presenter? = null
    private var count: Int = 1
    private var videoList = mutableListOf<Mv>()
    private val videoAdapter: VideoListAdapter by lazy { VideoListAdapter(videoList, context!!) }
    private var isFresh: Boolean = false
    override fun getLayoutResID(): Int {
        return R.layout.fragment_video_category
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        VideoPresent(this).subscribe(this)
        initView()
        initLoadingMore()
    }

    override fun setPresenter(presenter: VideoContract.Presenter) {
        mPresenter = presenter
    }


    override fun onVideoLoad(videoLists: List<Mv>) {
        if (isFresh) isFresh = false
        videoList.addAll(videoLists)
//        videoAdapter.addData(videoList)
        videoAdapter.notifyDataSetChanged()
    }

    override fun onVideoCommomLoad(response: MvCommentsResult) {
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("Mv", videoList[position])
        switch(VideoDetailFragment(), bundle)
    }

    private fun switch(fragment: Fragment, bundle: Bundle) {
        try {
            (activity as MainActivity).switchFragment(fragment, bundle)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRefresh() {
        if (!isFresh) {
            ++count
            isFresh = true
            load.isRefreshing = false
            showLoading()
            mPresenter?.loadVideo(count * 15, area)
        }
    }


    private fun initView() {
        mPresenter!!.loadVideo(area = area)
        videoAdapter.setOnItemListener(this)
        videoListView.layoutManager = LinearLayoutManager(context)
        videoListView.adapter = videoAdapter
    }

    private fun initLoadingMore() {
        load.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)
        load.setDistanceToTriggerSync(700)
        load.setProgressBackgroundColorSchemeColor(Color.WHITE)
        load.setSize(SwipeRefreshLayout.DEFAULT)
        load.setOnRefreshListener(this)
    }


}

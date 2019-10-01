package com.brins.lightmusic.ui.fragment.video


import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.utils.launch
import com.brins.lightmusic.utils.show
import kotlinx.android.synthetic.main.fragment_video_category.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        videoAdapter.setOnItemListener(this)
        videoListView.layoutManager = LinearLayoutManager(context)
        videoListView.adapter = videoAdapter
        initLoadingMore()

    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        VideoPresent(this).subscribe(this)
        initView()
    }

    override fun setPresenter(presenter: VideoContract.Presenter) {
        mPresenter = presenter
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
            loadMvData(count * 15)
        }
    }


    private fun initView() {
        loadMvData(0)
    }


    private fun loadMvData(limit :Int){
        launch({
            showLoading()
            videoList.addAll(loadMvData(limit,area))
            videoAdapter.notifyDataSetChanged()
            if (isFresh) isFresh = false
            hideLoading()
        },{
            Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    private suspend fun loadMvData(limit: Int = 0,area: String) = withContext(Dispatchers.IO){
        showLoading()
        if (limit == 0)
            mPresenter!!.loadVideo(area = area)
        else
            mPresenter!!.loadVideo(limit,area)
    }

    override fun showRetryView() {
        super.showRetryView()
        val textError = TextView(context)
        textError.setText(R.string.connect_error)
        textError.textSize = 20f
        textError.setTextColor(ContextCompat.getColor(context!!,R.color.translucent))
        val p : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        p.gravity = Gravity.CENTER
        textError.gravity = Gravity.CENTER
        recommendContainer.addView(textError,p)
        textError.setOnClickListener{
            recommendContainer.removeView(textError)
            loadMvData(0)
        }
    }

    private fun initLoadingMore() {
        load.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED)
        load.setDistanceToTriggerSync(700)
        load.setProgressBackgroundColorSchemeColor(Color.WHITE)
        load.setSize(SwipeRefreshLayout.DEFAULT)
        load.setOnRefreshListener(this)
    }


}

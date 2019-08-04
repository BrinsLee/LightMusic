package com.brins.lightmusic.ui.fragment.video

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.JZVideoPalyerView
import kotlinx.android.synthetic.main.fragment_discovery.*
import kotlinx.android.synthetic.main.fragment_discovery.loadingLayout
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment() , VideoContract.View, OnItemClickListener {


    var videoList = mutableListOf<Mv>()
    val videoAdapter : VideoListAdapter by lazy{ VideoListAdapter(videoList,context!!) }
    override fun getLayoutResID(): Int {
        return R.layout.fragment_video
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        initView()
    }

    private fun initView() {
        VideoPresent(this).subscribe()
        videoAdapter.setOnItemListener(this)
        videoListView.layoutManager = LinearLayoutManager(context)
        videoListView.adapter = videoAdapter
        videoListView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {

            }

            override fun onChildViewDetachedFromWindow(view: View) {
                val jz = view.findViewById<JZVideoPalyerView>(R.id.video_player)
                if (jz?.jzDataSource != null && Jzvd.CURRENT_JZVD != null && jz.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)){
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
    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun getcontext(): Context {
        return context!!
    }

    override fun handleError(error: Throwable) {
    }

    override fun onVideoLoad(videoLists: List<Mv>) {
        videoList.addAll(videoLists)
//        videoAdapter.addData(videoList)
        videoAdapter.notifyDataSetChanged()
    }

    override fun setPresenter(presenter: VideoContract.Presenter?) {
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }
}

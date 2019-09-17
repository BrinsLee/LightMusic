package com.brins.lightmusic.ui.fragment.video


import androidx.fragment.app.Fragment

import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseFragment



class VideoFragment : BaseFragment<VideoContract.Presenter>(){


    val adapter by lazy { MainPagerAdapter(childFragmentManager, list) }


    var list = mutableListOf<Fragment>(
        VideoCategoryFragment(),
        VideoCategoryFragment(),
        VideoCategoryFragment(),
        VideoCategoryFragment()
    )


    override fun getLayoutResID(): Int {
        return R.layout.fragment_video
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        initView()

    }

/*
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
*/

    private fun initView() {

    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    override fun setPresenter(presenter: VideoContract.Presenter) {

    }


}

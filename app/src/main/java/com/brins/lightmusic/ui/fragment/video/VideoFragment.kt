package com.brins.lightmusic.ui.fragment.video


import android.graphics.Color
import android.widget.TextView
import androidx.fragment.app.Fragment

import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.VideoPagerAdapter
import com.brins.lightmusic.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment<VideoContract.Presenter>() {


    var list = mutableListOf<Fragment>(
        VideoCategoryFragment(),
        VideoCategoryFragment(),
        VideoCategoryFragment(),
        VideoCategoryFragment(),
        VideoCategoryFragment()

    )
    val adapter by lazy { VideoPagerAdapter(childFragmentManager, list) }

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
        mViewpager.adapter = adapter
        mTablayout.setupWithViewPager(mViewpager)
        for (i in 0 until adapter.count) {
            mTablayout.getTabAt(i)!!.customView = adapter.getTabView(activity!!, i)
            mTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.WHITE)
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.GRAY)
                }

            })
        }
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    override fun setPresenter(presenter: VideoContract.Presenter) {

    }


}

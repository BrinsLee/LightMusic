package com.brins.lightmusic.ui.fragment.video


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

import cn.jzvd.Jzvd
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.VideoPagerAdapter
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment() {
    override fun initInject() {
        getFragmentComponent().inject(this)
    }


    private var list = mutableListOf<Fragment>(
        VideoCategoryFragment(MAINLAND),
        VideoCategoryFragment(HONGKONG_TAIWAN),
        VideoCategoryFragment(EUROPE_AMERICA),
        VideoCategoryFragment(JAPAN),
        VideoCategoryFragment(KOREA)

    )
    val adapter by lazy { VideoPagerAdapter(childFragmentManager, list) }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_video
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mViewpager.adapter = adapter
        mTablayout.setupWithViewPager(mViewpager)
        for (i in 0 until adapter.count) {
            mTablayout.getTabAt(i)!!.customView = adapter.getTabView(activity!!, i)
            mTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    val tv_tab = p0?.customView?.findViewById(R.id.tab_item) as? TextView
                    tv_tab?.setTextColor(Color.GRAY)
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    val tv_tab = p0?.customView?.findViewById(R.id.tab_item) as? TextView
                    tv_tab?.setTextColor(Color.BLACK)
                }

            })
        }
    }


}

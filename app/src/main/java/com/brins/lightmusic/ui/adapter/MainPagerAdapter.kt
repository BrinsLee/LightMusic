package com.brins.lightmusic.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.mainfragment.MainFragment
import com.brins.lightmusic.ui.fragment.minefragment.MineFragment
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private var list = mutableListOf<Fragment>(
        MainFragment(),
        DiscoveryFragment(),
        VideoFragment(),
        MineFragment()
    )

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}
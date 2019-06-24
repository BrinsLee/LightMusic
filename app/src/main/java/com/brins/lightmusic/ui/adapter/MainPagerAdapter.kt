package com.brins.lightmusic.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.lightmusic.R

class MainPagerAdapter(fm: FragmentManager, var list: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    val tabtitle = arrayOf("我的", "发现", "视频", "朋友")
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.main_tab, null)
        var tab_item: TextView = view.findViewById(R.id.tab_item)
        if (position == 0) {
            tab_item.textSize = 18f
        }
        tab_item.text = tabtitle[position]
        return view
    }
}
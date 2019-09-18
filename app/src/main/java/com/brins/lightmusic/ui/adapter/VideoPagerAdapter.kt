package com.brins.lightmusic.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.lightmusic.R

class VideoPagerAdapter(fm: FragmentManager, var list: MutableList<Fragment>) :
    FragmentPagerAdapter(fm) {
    val tabtitle = arrayOf("内地", "港台", "欧美", "日本", "韩国")

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.main_tab, null)
        var tab_item: TextView = view.findViewById(R.id.tab_item)
        tab_item.textSize = 15f
        tab_item.setTextColor(Color.WHITE)
        if (position == 0) {
            tab_item.setTextColor(Color.GRAY)
        }
        tab_item.text = tabtitle[position]
        return view
    }
}
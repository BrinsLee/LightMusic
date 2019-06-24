package com.brins.lightmusic.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.localmusic.LocalMusicFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var list = mutableListOf<Fragment>()
    val adapter by lazy { MainPagerAdapter(supportFragmentManager, list) }

    companion object {
        fun startThisActivity(activity: AppCompatActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        initViewPagerAndTabLay()
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        list.add(LocalMusicFragment())
        list.add(LocalMusicFragment())
        list.add(LocalMusicFragment())
        list.add(LocalMusicFragment())
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        for (i in 0 until adapter.count) {
            tab_layout.getTabAt(i)!!.customView = adapter.getTabView(this, i)
        }
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var tv_tab = tab!!.customView!!.findViewById(R.id.tab_item) as TextView
                tv_tab.textSize = 12f
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var tv_tab = tab!!.customView!!.findViewById(R.id.tab_item) as TextView
                tv_tab.textSize = 18f
            }

        })

    }
}

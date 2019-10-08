package com.brins.lightmusic.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.artists.ArtistFragment
import com.brins.lightmusic.ui.fragment.myfragment.MyFragment
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_common_toolbar.*




class MainActivity : BaseActivity(){


    var list = mutableListOf<Fragment>()
    val adapter by lazy { MainPagerAdapter(supportFragmentManager, list) }
    var currentFragment: Fragment? = null


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

    override fun onStart() {
        super.onStart()
        showBottomBar(supportFragmentManager)
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        list.add(MyFragment())
        list.add(DiscoveryFragment())
        list.add(VideoFragment())
        list.add(ArtistFragment())
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        for (i in 0 until adapter.count) {
            tab_layout.getTabAt(i)!!.customView = adapter.getTabView(this, i)
        }
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tv_tab = tab!!.customView!!.findViewById(R.id.tab_item) as TextView
                tv_tab.textSize = 12f
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tv_tab = tab!!.customView!!.findViewById(R.id.tab_item) as TextView
                tv_tab.textSize = 18f
            }

        })

    }


    fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.add(
                R.id.fragment_container, targetFragment
                , targetFragment::class.java.name
            )
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.show(targetFragment)
        }
        currentFragment = targetFragment
        return transaction
    }

    fun switchFragment(targetFragment: Fragment, bundle: Bundle): FragmentTransaction {
        targetFragment.arguments = bundle
        return switchFragment(targetFragment)

    }

    override fun onDestroy() {
        super.onDestroy()
        removeBottomBar(supportFragmentManager)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                var second = System.currentTimeMillis()
                if (second - firstTime > 2000) {
                    Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()
                    firstTime = second
                    return true
                } else {
                    System.exit(0)
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                SearchActivity.startThis(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

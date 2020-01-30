package com.brins.lightmusic.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import butterknife.ButterKnife
import butterknife.OnClick
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.artists.ArtistFragment
import com.brins.lightmusic.ui.fragment.myfragment.MyFragment
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import com.brins.lightmusic.utils.setColorTranslucent
import com.brins.lightmusic.utils.setTranslucent
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottombar.*
import kotlinx.android.synthetic.main.view_common_toolbar.*


class MainActivity : AppCompatActivity() {


    private var currentPage = 0
    private var list = mutableListOf<Fragment>()
    private val adapter by lazy { MainPagerAdapter(supportFragmentManager, list) }
    private var currentFragment: Fragment? = null
    private var mClickTime: Long = 0



    companion object {
        fun startThisActivity(activity: AppCompatActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setColorTranslucent(this)
        initViewPagerAndTabLay()
    }


    override fun onStart() {
        super.onStart()
//        showBottomBar(supportFragmentManager)
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        list.add(MyFragment())
        list.add(DiscoveryFragment())
        list.add(VideoFragment())
        list.add(ArtistFragment())
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3
        changeTab(0)
    }

    private fun changeTab(position: Int) {
        tab_main_btn.isSelected = false
        tab_main_tv.isSelected = false
        tab_discovery_btn.isSelected = false
        tab_discovery_tv.isSelected = false
        tab_video_btn.isSelected = false
        tab_video_tv.isSelected = false
        tab_singer_btn.isSelected = false
        tab_singer_tv.isSelected = false
        currentPage = position

        when (position) {
            0 -> {
                tab_main_btn.isSelected = true
                tab_main_tv.isSelected = true
                tv_title.text = getString(R.string.app_name)
            }
            1 -> {
                tab_discovery_tv.isSelected = true
                tab_discovery_btn.isSelected = true
                tv_title.text = getString(R.string.discovery_tab)
            }
            2 -> {
                tab_video_btn.isSelected = true
                tab_video_tv.isSelected = true
                tv_title.text = getString(R.string.video_tab)

            }
            3 -> {
                tab_singer_btn.isSelected = true
                tab_singer_tv.isSelected = true
                tv_title.text = getString(R.string.singers)
            }
        }
        view_pager.currentItem = position

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
/*
        removeBottomBar(supportFragmentManager)
*/
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (SystemClock.elapsedRealtime() - mClickTime > 1000) {
                    Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()
                    mClickTime = SystemClock.elapsedRealtime()
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

    @OnClick(
        R.id.tab_main_btn,
        R.id.tab_main_tv,
        R.id.tab_discovery_btn,
        R.id.tab_discovery_tv,
        R.id.tab_video_btn,
        R.id.tab_video_tv,
        R.id.tab_singer_btn,
        R.id.tab_singer_tv
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tab_main_btn,
            R.id.tab_main_tv -> {
                changeTab(0)
            }
            R.id.tab_discovery_btn,
            R.id.tab_discovery_tv -> {
                changeTab(1)
            }
            R.id.tab_video_btn,
            R.id.tab_video_tv -> {
                changeTab(2)
            }
            R.id.tab_singer_btn,
            R.id.tab_singer_tv -> {
                changeTab(3)
            }
        }
    }

}

package com.brins.lightmusic.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.friends.FriendsFragment
import com.brins.lightmusic.ui.fragment.localmusic.LocalMusicFragment
import com.brins.lightmusic.ui.fragment.myfragment.MyFragment
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    var list = mutableListOf<Fragment>()
    val adapter by lazy { MainPagerAdapter(supportFragmentManager, list) }
    var currentFragment : BaseFragment? = null
    var currentMusicListId : String = ""

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
        showBottomBar()
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        list.add(MyFragment())
        list.add(DiscoveryFragment())
        list.add(VideoFragment())
        list.add(FriendsFragment())
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

     fun switchFragment(currentId : String,targetFragment : BaseFragment) : FragmentTransaction{
         currentMusicListId = currentId
         val transaction = supportFragmentManager.beginTransaction()
         if(!targetFragment.isAdded){
             if (currentFragment != null){
                 transaction.hide(currentFragment!!)
             }
             transaction.add(R.id.drawer,targetFragment
                 ,targetFragment::class.java.name)
         }else{
             if (currentFragment != null){
                 transaction.hide(currentFragment!!)
             }
             transaction.show(targetFragment)
         }
         currentFragment = targetFragment
         return transaction

     }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                var second = System.currentTimeMillis()
                if (second - firstTime > 2000) {
                    Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()
                    firstTime = second
                    return true
                }else {
                    System.exit(0)
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }
}

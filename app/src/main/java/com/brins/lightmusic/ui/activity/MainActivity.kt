package com.brins.lightmusic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import butterknife.ButterKnife
import butterknife.OnClick
import com.brins.lightmusic.R
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.adapter.MainPagerAdapter
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.mainfragment.MainFragment
import com.brins.lightmusic.ui.fragment.minefragment.MineFragment
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerContract
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerPresenter
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import com.brins.lightmusic.utils.getStatusBarHeight
import com.brins.lightmusic.utils.setTextDark
import com.brins.lightmusic.utils.setTranslucent
import com.brins.lightmusic.utils.string2Bitmap
import com.hwangjr.rxbus.annotation.Subscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottombar.*
import kotlinx.android.synthetic.main.view_common_toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MusicPlayerContract.View, IPlayback.Callback {


    private var currentPage = 0
    private var list = mutableListOf<Fragment>()
    private val adapter by lazy { MainPagerAdapter(supportFragmentManager, list) }
    private var mClickTime: Long = 0
    @Inject
    lateinit var mPresenter: MusicPlayerPresenter
    private var index = -1
    private var mPlayer: PlayBackService? = null
    private var type = -1
    private lateinit var playList: PlayList
    private val mHandler = Handler()
    private var mProgressCallback = object : Runnable {
        override fun run() {
            if (mPlayer != null && mPlayer!!.isPlaying()) {
                val progress =
                    cover_container.getMax() * mPlayer!!.getProgress() / getCurrentSongDuration()
                if (progress >= 0 && progress <= cover_container.getMax()) {
                    cover_container.setProgress(progress)
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL)
                }
            }
        }

    }


    companion object {
        private val UPDATE_PROGRESS_INTERVAL: Long = 1000
        private val UPDATE_MUSIC_INTERVAL: Long = 500
        fun startThisActivity(activity: AppCompatActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun handleError(error: Throwable) {
    }

    override fun isSubscribe(): Boolean {
        return true
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        ButterKnife.bind(this)
        setTranslucent(this)
        setTextDark(this.window, true)
        toolbar.setPadding(0, getStatusBarHeight(this), 0, 0)
        initViewPagerAndTabLay()
        mPresenter.subscribe(this)
    }


    override fun onStart() {
        super.onStart()
        if (mPlayer != null && mPlayer!!.isPlaying()) {
            mHandler.removeCallbacks(mProgressCallback)
            mHandler.post(mProgressCallback)
        }
    }

    override fun onStop() {
        super.onStop()
        mPresenter.unsubscribe()
        mHandler.removeCallbacks(mProgressCallback)
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        list.add(MainFragment())
        list.add(DiscoveryFragment())
        list.add(VideoFragment())
        list.add(MineFragment())
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
                tv_title.text = getString(R.string.mine)
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



    @Subscribe
    fun onPlayMusic(playListEvent: PlayListEvent) {
        type = playListEvent.type
        playList = playListEvent.playlist
        index = playListEvent.playIndex
//        playSong(playList, index)
        mPlayer?.setPlayList(playList)
        mPlayer?.play(index)
        Log.d("RxBus:", "QuickControlFragment")
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
        R.id.tab_singer_tv,
        R.id.cover_container
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
            R.id.cover_container -> {
                if (mPlayer != null && ::playList.isInitialized) {
                    MusicPlayActivity.startThisActivity(this@MainActivity)
                }
                return
            }
        }
    }


    override fun onPlaybackServiceBound(service: PlayBackService) {
        mPlayer = service
        mPlayer!!.registerCallback(this)
    }

    override fun onPlaybackServiceUnbound() {
        mPlayer!!.unregisterCallback(this)
        mPlayer = null
    }

    override fun onSongSetAsFavorite(song: Music?) {

    }

    override fun onSongUpdated(song: Music?) {
        if (song == null) {
            cover_container.setProgress(0)
            cover_container.cancelRotateAnimation()
            mHandler.removeCallbacks(mProgressCallback)
            return
        }
        try {
            if (song.bitmapCover == null) {
                val bitmap = string2Bitmap(song.album.picUrl)
                song.bitmapCover = bitmap

            }
            cover_container.setImageBitmap(song.bitmapCover)
            cover_container.setProgress(initProgress(mPlayer!!.getProgress()))
            if (mPlayer!!.isPlaying()) {
                cover_container.startRotateAnimation()
                mHandler.post(mProgressCallback)
            } else {
                cover_container.pauseRotateAnimation()
                mHandler.removeCallbacks(mProgressCallback)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    override fun updatePlayMode(playMode: PlayMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePlayToggle(play: Boolean) {
        if (cover_container == null) {
            return
        }
        if (play) {
            cover_container.resumeRotateAnimation()
        } else {
            cover_container.pauseRotateAnimation()
        }
    }

    override fun updateFavoriteToggle(favorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayStatusChanged(isPlaying: Boolean, music: Music?) {
        updatePlayToggle(isPlaying)
        if (music != null) {
            onSongUpdated(music)
        }
    }

    private fun initProgress(progress: Int): Int {
        return cover_container.getMax() * progress / getCurrentSongDuration()
    }

    private fun getCurrentSongDuration(): Int {
        val currentSong = mPlayer!!.getPlayingSong()
        var duration = 0
        if (currentSong != null) {
            duration = currentSong.duration
        }
        return duration
    }
}

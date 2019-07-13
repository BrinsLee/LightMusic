package com.brins.lightmusic.ui.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.*
import android.transition.Explode
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brins.lightmusic.R
import com.brins.lightmusic.common.AppConfig
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.customview.CustPagerTransformer
import com.brins.lightmusic.ui.customview.RoundCoverImageView
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerContract
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerPresenter
import com.brins.lightmusic.utils.AlbumUtils.Companion.String2Bitmap
import com.brins.lightmusic.utils.FastBlurUtil
import com.brins.lightmusic.utils.HandlerUtil
import com.brins.lightmusic.utils.TimeUtils
import com.bytedance.sdk.openadsdk.core.widget.RoundImageView
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.include_play_control.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


class MusicPlayActivity : BaseActivity(), MusicPlayerContract.View, IPlayback.Callback, View.OnClickListener {

    private var mPlayer: IPlayback? = null
    private var isPlaying = false
    private lateinit var mPresenter: MusicPlayerContract.Presenter
    private val mHamdler: HandlerUtil by lazy { HandlerUtil.getInstance(this) }
    private val mUpAlbumRunnable = Runnable { SetBlurredAlbumArt().execute() }
    private val mPlayList: PlayList by lazy { getCurrentList()!! }
    private var current: Music? = null
    private var cover: Bitmap? = null
    private var musics: MutableList<Music>? = null
    private val mHandler = Handler()
    private val mMusicHandler =
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MOVE_TO_NEXT -> onPlayNext()
                    MOVE_TO_LAST -> onPlayLast()
                    else -> {
                    }
                }
                super.handleMessage(msg)

            }
        }
    private val UPDATE_PROGRESS_INTERVAL: Long = 1000
    private val UPDATE_MUSIC_INTERVAL: Long = 500

    private val MOVE_TO_NEXT = 1
    private val MOVE_TO_LAST = 2
    private var pageAdapter: PagerAdapter? = null
    val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private var mProgressCallback = object : Runnable {
        override fun run() {
            if (mPlayer != null && mPlayer!!.isPlaying()) {
                val progress = seekBar.max * mPlayer!!.getProgress() / getCurrentSongDuration()
                updateProgressTextWithDuration(mPlayer!!.getProgress())
                if (progress >= 0 && progress <= seekBar.max) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true)
                    } else {
                        seekBar.progress = progress
                    }
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL)
                }
            }
        }

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_play
    }

    companion object {

        @JvmStatic
        private var PLAYINDEX = "playindex"
        @JvmStatic
        var mImageViewList = mutableListOf<ImageView>()

        fun startThisActivity(activity: AppCompatActivity, isPlaying: Boolean) {
            val intent = Intent(activity, MusicPlayActivity::class.java)
            intent.putExtra(PLAYINDEX, isPlaying)
            activity.startActivity(intent)
        }
    }


    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        setSupportActionBar(toolBar)
        supportActionBar!!.title = ""
        MusicPlayerPresenter.instance.setContext(this).setView(this).subscribe()
        isPlaying = intent.getBooleanExtra(PLAYINDEX, false)
        ivCover.setPageTransformer(false, CustPagerTransformer())
        setPlayView()
    }

    fun setPlayView() {
        current = mPlayList.getCurrentSong()
        musics = mPlayList.getSongs()
        if (mImageViewList.size == 0) {
            for (i in 0 until mPlayList.getNumOfSongs()) {
                val imageView = RoundImageView(applicationContext)
                imageView.setImageBitmap(String2Bitmap(musics!![i].cover!!))
                imageView.layoutParams = ViewGroup.LayoutParams(100, 100)
                imageView.scaleType = ImageView.ScaleType.CENTER
                mImageViewList.add(imageView)
            }
        }

        if (current != null) {
            initView()
            setListener()
        }
    }

    private fun initView() {
        onSongUpdated(current)
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
        mHandler.removeCallbacks(mProgressCallback)
    }

    private fun updateProgressTextWithProgress(progress: Int) {
        tvProgress.text = TimeUtils.formatDuration(progress)
    }

    private fun updateProgressTextWithDuration(duration: Int) {
        tvProgress.text = TimeUtils.formatDuration(duration)
    }

    private fun getDuration(progress: Int): Int {
        return (getCurrentSongDuration() * (progress.toFloat() / seekBar.max)).toInt()
    }

    private fun initProgress(progress: Int): Int {
        return seekBar.max * progress / getCurrentSongDuration()
    }

    private fun getCurrentSongDuration(): Int {
        val currentSong = mPlayer!!.getPlayingSong()
        var duration = 0
        if (currentSong != null) {
            duration = currentSong.duration
        }
        return duration
    }

    //click event

    fun setListener() {
        playOrPause.setOnClickListener(this)
        preSong.setOnClickListener(this)
        nextSong.setOnClickListener(this)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mHandler.removeCallbacks(mProgressCallback)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                seekTo(getDuration(seekBar.progress))
                if (mPlayer!!.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback)
                    mHandler.post(mProgressCallback)
                }
            }

        })
        ivCover.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Log.d(TAG, "currentIndex be: ${mPlayList.getPlayingIndex()}")
                if (position > mPlayList.getPlayingIndex()) {
                    val msg = mMusicHandler.obtainMessage()
                    msg.what = MOVE_TO_NEXT
                    mMusicHandler.sendMessageDelayed(msg, UPDATE_MUSIC_INTERVAL)
                } else if (position < mPlayList.getPlayingIndex()) {
                    val msg = mMusicHandler.obtainMessage()
                    msg.what = MOVE_TO_LAST
                    mMusicHandler.sendMessageDelayed(msg, UPDATE_MUSIC_INTERVAL)
                }
                Log.d(TAG, "position: $position")
                Log.d(TAG, "currentIndex af: ${mPlayList.getPlayingIndex()}")
            }

        })
    }

    private fun seekTo(duration: Int) {
        mPlayer!!.seekTo(duration)
    }

    override fun onClick(v: View?) {
        when (v) {
            playOrPause -> {
                onPlayPauseToggle()
            }
            nextSong -> {
                onPlayNext()
            }
            preSong -> {
                onPlayLast()
            }
        }
    }

    private fun onPlayLast() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.playLast()
    }

    private fun onPlayNext() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.playNext()
    }

    fun onPlayPauseToggle() {
        if (mPlayer == null) {
            return
        }
        isPlaying = if (mPlayer!!.isPlaying()) {
            mPlayer!!.pause()
            playOrPause.setImageResource(R.drawable.ic_playmusic)
            false
        } else {
            mPlayer!!.play()
            playOrPause.setImageResource(R.drawable.ic_pausemusic)
            true
        }
    }


    inner class SetBlurredAlbumArt : AsyncTask<Void, Void, Drawable>() {
        override fun doInBackground(vararg params: Void?): Drawable {
            return getForegroundDrawable(cover!!)
        }

        override fun onPostExecute(result: Drawable?) {
            setDrawable(result)
        }
    }

    private fun setDrawable(result: Drawable?) {
        if (result != null) {
            if (rootLayout.background != null) {
                val td = TransitionDrawable(arrayOf(rootLayout.background, result))
                rootLayout.background = td
                td.isCrossFadeEnabled = true
                td.startTransition(200)
            } else {
                rootLayout.background = result
            }
        }
    }

    fun getForegroundDrawable(bitmap: Bitmap): Drawable {
        val widthHeightSize =
            (AppConfig.DisplayUtil.getScreenWidth(this@MusicPlayActivity) * 1.0 / AppConfig.DisplayUtil.getScreenHeight(
                this
            ) * 1.0).toFloat()
        val cropBitmapWidth = (widthHeightSize * bitmap.height).toInt()
        val cropBitmapWidthX = ((bitmap.width - cropBitmapWidth) / 2.0).toInt()

        val cropBitmap = Bitmap.createBitmap(
            bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
            bitmap.height
        )
        val scaleBitmap = Bitmap.createScaledBitmap(
            cropBitmap, bitmap.width / 50, bitmap
                .height / 50, false
        )
        val blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true)
        val foregroundDrawable = BitmapDrawable(resources, blurBitmap)
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        return foregroundDrawable

    }

    fun initViewPager() {
        if (pageAdapter == null) {
            pageAdapter = object : PagerAdapter() {
                override fun isViewFromObject(view: View, ob: Any): Boolean {
                    return view == ob
                }

                override fun instantiateItem(container: ViewGroup, position: Int): Any {
                    val imageView = mImageViewList[position]
                    if (imageView.parent != null) {
                        (imageView.parent as ViewGroup).removeView(imageView)
                    }
                    container.addView(imageView)
                    return imageView
                }

                override fun destroyItem(container: ViewGroup, position: Int, ob: Any) {
                    container.removeView(ob as View)
                }

                override fun getCount(): Int {
                    return mImageViewList.size
                }
            }
        }
        ivCover.adapter = pageAdapter
        ivCover.currentItem = mPlayList.getPlayingIndex()
    }

    //MVP View
    override fun handleError(error: Throwable) {

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onSongUpdated(song: Music?) {

        if (song == null) {
            playOrPause.setImageResource(R.drawable.ic_playmusic)
            seekBar.progress = 0
            updateProgressTextWithProgress(0)
            seekTo(0)
            mHandler.removeCallbacks(mProgressCallback)
            return
        }
        cover = String2Bitmap(song.cover!!)
        initViewPager()
        mHamdler.postDelayed(mUpAlbumRunnable, 200)
        musicTitle.text = song.name
        musicArtist.text = song.singer
        tvDuration.text = TimeUtils.formatDuration(song.duration)
        seekBar.progress = initProgress(mPlayer!!.getProgress())
        updateProgressTextWithProgress(mPlayer!!.getProgress())
        mHandler.removeCallbacks(mProgressCallback)
        if (mPlayer!!.isPlaying()) {
            playOrPause.setImageResource(R.drawable.ic_pausemusic)
            mHandler.post(mProgressCallback)
        } else {
            playOrPause.setImageResource(R.drawable.ic_playmusic)
        }
    }


    override fun updatePlayMode(playMode: PlayMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePlayToggle(play: Boolean) {
        playOrPause.setImageResource(if (play) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
    }

    override fun updateFavoriteToggle(favorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: MusicPlayerContract.Presenter) {
        mPresenter = presenter
    }

    //IPlayback
    override fun onSwitchLast(last: Music) {
        onSongUpdated(last)
    }

    override fun onSwitchNext(next: Music) {
        onSongUpdated(next)
    }

    override fun onComplete(next: Music?) {
        onSongUpdated(next)
    }

    override fun onPlayStatusChanged(isPlaying: Boolean) {
        updatePlayToggle(isPlaying)
        if (isPlaying) {
            mHandler.removeCallbacks(mProgressCallback)
            mHandler.post(mProgressCallback)
        } else {
            mHandler.removeCallbacks(mProgressCallback)

        }
    }

}

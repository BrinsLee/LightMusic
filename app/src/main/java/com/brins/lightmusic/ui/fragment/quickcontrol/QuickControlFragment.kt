package com.brins.lightmusic.ui.fragment.quickcontrol

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.activity.MusicPlayActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.string2Bitmap
import com.hwangjr.rxbus.annotation.Subscribe
import kotlinx.android.synthetic.main.fragment_quick_control.*
import java.lang.Exception
import javax.inject.Inject


class QuickControlFragment : BaseFragment(), MusicPlayerContract.View, IPlayback.Callback,
    View.OnClickListener{
    override fun initInject() {
        getFragmentComponent().inject(this)
    }


    @Inject
    lateinit var mPresenter: MusicPlayerPresenter
    private var index = -1
    private var mPlayer: PlayBackService? = null
    private var type = -1
    private lateinit var playList: PlayList
    override fun getLayoutResID(): Int {
        return R.layout.fragment_quick_control
    }

    companion object {
        var Instance: QuickControlFragment? = null
        @JvmStatic
        fun newInstance(): QuickControlFragment {
            return if (Instance == null) {
                Instance = QuickControlFragment()
                Instance!!
            } else {
                Instance!!
            }

        }
    }

    override fun onCreateViewAfterBinding() {
        super.onCreateViewAfterBinding()
        RxBus.getInstance().register(this)
    }

    fun getCurrentList(): PlayList {
        return playList
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


    override fun onStart() {
        super.onStart()
        MusicPlayerPresenter.instance.subscribe(this)

    }

    override fun onResume() {
        super.onResume()
        if (mPlayer != null) {
            ivPlayOrPause.setImageResource(if (mPlayer!!.isPlaying()) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
            onPlayStatusChanged(mPlayer!!.isPlaying(),mPlayer!!.getPlayingSong())
        }
        setListener()
    }


    fun playOrPause() {
        Log.d(TAG, "headSet_PlayOrPause")
        mPlayer?.pause()
    }

    fun playNext() {
        Log.d(TAG, "headSet_PlayNext")
        if (playList.getNumOfSongs() == 1) {
            return
        }
        onPlayNext()
    }

    fun playPrevious() {
        Log.d(TAG, "headSet_PlayLast")
        if (playList.getNumOfSongs() == 1) {
            return
        }
        onPlayLast()
    }

    private fun setListener() {
        ivPlayOrPause.setOnClickListener(this)
        ivPlaybarPre.setOnClickListener(this)
        ivPlaybarNext.setOnClickListener(this)
        playBarLayout.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        RxBus.getInstance().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }

    // Click Events
    override fun onClick(v: View) {

        when (v.id) {
            R.id.ivPlayOrPause -> {
                onPlayPauseToggle()
            }
            R.id.ivPlaybarPre -> {
                playPrevious()
            }
            R.id.ivPlaybarNext -> {
                playNext()
            }
            R.id.playBarLayout -> {
                if (mPlayer != null && ::playList.isInitialized) {
                    MusicPlayActivity.startThisActivity((activity as AppCompatActivity))
                }
                return
            }
        }
    }


    fun onPlayPauseToggle() {
        if (mPlayer == null || !::playList.isInitialized) {
            return
        }
        if (mPlayer!!.isPlaying()) {
            mPlayer!!.pause()
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
        } else {
            mPlayer!!.play()
            ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
        }
    }


    fun onPlayLast() {
        if (mPlayer == null) return
        mPlayer!!.playLast()
        return
    }

    fun onPlayNext() {
        if (mPlayer == null) return
        mPlayer!!.playNextSong()
        return
    }

    override fun onPlayStatusChanged(isPlaying: Boolean, music: Music?) {
        if (ivPlayOrPause == null){
            return
        }else {
            updatePlayToggle(isPlaying)
            if (music != null){
                onSongUpdated(music)
            }
        }
    }

    // MVP View



    override fun onPlaybackServiceBound(service: PlayBackService) {
        mPlayer = service
        mPlayer!!.registerCallback(this)
    }

    override fun updatePlayMode(playMode: PlayMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePlayToggle(play: Boolean) {
        try {
            ivPlayOrPause.setImageResource(if (play) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
            if (play) {
                ivPlaybarCover.resumeRotateAnimation()
            } else {
                ivPlaybarCover.pauseRotateAnimation()
            }
        }catch (e : Exception){
            return
        }
    }

    override fun updateFavoriteToggle(favorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun handleError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlaybackServiceUnbound() {
//        mPlayer!!.unregisterCallback(this)
    }

    override fun onSongSetAsFavorite(song: Music?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 更新UI
     */
    override fun onSongUpdated(song: Music?) {
        if (song == null) {
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
            return
        }
        try {
            tvPlaybarTitle.text = song.name
            tvPlaybarArtist.text = song.artistBeans!![0].name
            if (song.bitmapCover == null) {
                val bitmap = string2Bitmap(song.album.picUrl)
                song.bitmapCover = bitmap

            }
//            ivPlaybarCover.setImageBitmap(song.bitmapCover)
            ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

    }

}

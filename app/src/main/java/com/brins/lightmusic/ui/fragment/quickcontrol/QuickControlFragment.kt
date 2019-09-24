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
import com.brins.lightmusic.player.broadcast.HeadsetButtonReceiver
import com.brins.lightmusic.ui.activity.MusicPlayActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.TYPE_LOCAL_MUSIC
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.string2Bitmap
import com.hwangjr.rxbus.annotation.Subscribe
import kotlinx.android.synthetic.main.fragment_quick_control.*
import java.lang.Exception


class QuickControlFragment : BaseFragment<MusicPlayerContract.Presenter>(), MusicPlayerContract.View, IPlayback.Callback,
    View.OnClickListener,
    HeadsetButtonReceiver.onHeadsetListener {


    private var index = -1
    private var mPlayer: PlayBackService? = null
    private var type = -1
    private lateinit var playList: PlayList
    private lateinit var mPresenter: MusicPlayerContract.Presenter
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

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
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
        playSong(playList, index)
        Log.d("RxBus:", "QuickControlFragment")
    }

    private fun playSong(playList: PlayList, index: Int) {
        playList.setPlayMode(PlayMode.getDefault())

        val song = playList.getCurrentSong()
        when (type) {
            TYPE_ONLINE_MUSIC -> {
                mPresenter.loadMusicDetail(song!!)
            }
            TYPE_LOCAL_MUSIC -> {
                mPlayer!!.play(playList, index)
            }
            else -> {
                return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        HeadsetButtonReceiver(this)
        MusicPlayerPresenter.instance.subscribe(this)

    }

    override fun onResume() {
        super.onResume()
        if (mPlayer != null) {
            ivPlayOrPause.setImageResource(if (mPlayer!!.isPlaying()) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
            onPlayStatusChanged(mPlayer!!.isPlaying())

        }
        setListener()
    }


    //耳机线控回调
    override fun playOrPause() {
        Log.d(TAG, "headSet_PlayOrPause")
        mPlayer?.pause()
    }

    override fun playNext() {
        Log.d(TAG, "headSet_PlayNext")
        if (playList.getNumOfSongs() == 1) {
            return
        }
        onPlayNext()
    }

    override fun playPrevious() {
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
        if (::mPresenter.isInitialized) {
            mPresenter.unsubscribe()
        }
    }

    // Click Events
    override fun onClick(v: View) {

        when (v.id) {
            R.id.ivPlayOrPause -> {
                onPlayPauseToggle()
            }
            R.id.ivPlaybarPre -> {
                onPlayLast()
            }
            R.id.ivPlaybarNext -> {
                onPlayNext()
            }
            R.id.playBarLayout -> {
                if (mPlayer != null && ::playList.isInitialized) {
                    MusicPlayActivity.startThisActivity(
                        (activity as AppCompatActivity),
                        mPlayer!!.isPlaying(),
                        type)
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
        val hasLast = playList.hasLast()
        if (hasLast) {
            val song = playList.getSongs()[playList.getPlayingIndex() - 1]
            if (song.fileUrl == null || song.fileUrl == "") {
                index = playList.getPlayingIndex() - 1
                mPresenter.loadMusicDetail(song)
                return
            }
            mPlayer!!.playLast()
        }
        return
    }

    fun onPlayNext() {
        if (mPlayer == null) return
        val hasNext = playList.hasNext(false)
        if (hasNext) {
            val song = playList.getCurrentSong()!!
            if (song.fileUrl == null || song.fileUrl == "") {
                Log.d(TAG, "index: $index")
                index = playList.getPlayingIndex()
                Log.d(TAG, "index_after: ${playList.getPlayingIndex()}")
                mPresenter.loadMusicDetail(song)
                return
            }
            mPlayer!!.playNext()
        }
        return
    }


    // Player Callbacks
    override fun onSwitchLast(last: Music) {
        onSongUpdated(last)
    }

    override fun onSwitchNext(next: Music) {
        onSongUpdated(next)
    }

    override fun onComplete(next: Music?) {
        when (type) {
            TYPE_ONLINE_MUSIC -> {
                if (next!!.fileUrl == null || next.fileUrl == "" || next.bitmapCover == null) {
                    playNext()
                } else {
                    onSongUpdated(next)
                }
            }
            TYPE_LOCAL_MUSIC -> {
                onSongUpdated(next)
            }
            else -> {
                return
            }
        }
    }

    override fun onPlayStatusChanged(isPlaying: Boolean) {
        if (ivPlayOrPause == null){
            return
        }else {
            updatePlayToggle(isPlaying)
            if (isPlaying) {
                ivPlaybarCover.resumeRotateAnimation()
            } else {
                ivPlaybarCover.pauseRotateAnimation()
            }
        }
    }


    // MVP View

    override fun onMusicDetail(onlineMusic: Music) {
        Log.d(TAG, "index: $index")
        Log.d(TAG, "name: ${onlineMusic.name}")
        playList.setSong(onlineMusic, index)
        mPlayer!!.play(playList, index)
    }


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
        }catch (e : Exception){
            return
        }
    }

    override fun updateFavoriteToggle(favorite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: MusicPlayerContract.Presenter) {
        mPresenter = presenter
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
            ivPlaybarCover.setImageBitmap(song.bitmapCover)
            ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
            ivPlaybarCover.startRotateAnimation()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

    }

}

package com.brins.lightmusic.ui.fragment.quickcontrol

import android.os.Bundle
import android.view.View
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.base.BaseFragment
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_quick_control.*

class QuickControlFragment : BaseFragment(), MusicPlayerContract.View, IPlayback.Callback, View.OnClickListener {

    private var mPlayer: IPlayback? = null
    lateinit var playList: PlayList
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

        RxBus.getInstance().subscribe(Any::class.java
            , Consumer {
                when (it) {
                    is PlayListEvent -> onPlayMusic(it)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MusicPlayerPresenter(activity!!, this).subscribe()
        setListener()
    }

    private fun setListener() {
        ivPlayOrPause.setOnClickListener(this)
        ivPlaybarPre.setOnClickListener(this)
        ivPlaybarNext.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (mPlayer != null) {
            ivPlayOrPause.setImageResource(if (mPlayer!!.isPlaying()) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
        }
    }

    override fun onDestroyView() {
        mPresenter.unsubscribe()
        super.onDestroyView()
    }

    // Click Events
    override fun onClick(v: View) {

        when (v.id) {
            R.id.ivPlayOrPause -> {
                onPlayPauseToggle()
            }
            R.id.ivPlaybarPre -> {
                if (mPlayer == null) return
                mPlayer!!.playLast()
            }
            R.id.ivPlaybarNext -> {
                if (mPlayer == null) return
                mPlayer!!.playNext()
            }
            R.id.playBarLayout -> {

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

    private fun onPlayMusic(playListEvent: PlayListEvent) {

        playList = playListEvent.playlist
        val index = playListEvent.playIndex
        playSong(playList, index)
    }

    private fun playSong(playList: PlayList, index: Int) {
        playList.setPlayMode(PlayMode.getDefault())
        mPlayer!!.play(playList, index)
        val song = playList.getCurrentSong()
        onSongUpdated(song)
    }

    private fun playSong(song: Music) {
        val playList = PlayList(song)
        playSong(playList, 0)
    }


    // Player Callbacks
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
            ivPlaybarCover.resumeRotateAnimation()
        } else {
            ivPlaybarCover.pauseRotateAnimation()
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
        ivPlayOrPause.setImageResource(if (play) R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSongSetAsFavorite(song: Music?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSongUpdated(song: Music?) {
        if (song == null) {
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
            return
        }
        tvPlaybarTitle.text = song.title
        tvPlaybarArtist.text = song.singer
        ivPlaybarCover.setImageBitmap(song.cover)
        ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
        ivPlaybarCover.startRotateAnimation()

    }
}

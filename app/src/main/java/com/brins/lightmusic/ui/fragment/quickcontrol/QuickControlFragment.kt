package com.brins.lightmusic.ui.fragment.quickcontrol

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

class QuickControlFragment : BaseFragment(), MusicPlayerContract.View , IPlayback.Callback {


    private var mPlayer: IPlayback? = null
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

    private fun onPlayMusic(playListEvent: PlayListEvent) {

        val playList: PlayList = playListEvent.playlist
        val index = playListEvent.playIndex
        playSong(playList, index)
    }

    private fun playSong(playList: PlayList, index: Int) {
        playList.setPlayMode(PlayMode.getDefault())
        mPlayer!!.play(playList, index)
        /*val song = playList.getCurrentSong()
        onSongUpdated(song!!)*/
    }

    private fun playSong(song: Music) {
        val playList = PlayList(song)
        playSong(playList, 0)
    }

    // Player Callbacks

    override fun onSwitchLast(last: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwitchNext(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayStatusChanged(isPlaying: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun onSongSetAsFavorite(song: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSongUpdated(song: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

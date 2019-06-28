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
import kotlinx.android.synthetic.main.item_local_music.*

class QuickControlFragment : BaseFragment(), MusicPlayerContract.View , IPlayback.Callback , View.OnClickListener{

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MusicPlayerPresenter(activity!!,this).subscribe()
    }

    override fun onStart() {
        super.onStart()
        if(mPlayer != null){
            ivPlayOrPause.setImageResource(if (mPlayer!!.isPlaying())R.drawable.ic_pausemusic else R.drawable.ic_playmusic)
        }
    }

    override fun onDestroyView() {
        mPresenter.unsubscribe()
        super.onDestroyView()
    }

    // Click Events
    override fun onClick(v: View) {

        when(v.id){
            R.id.ivPlayOrPause -> {onPlayPauseToggle()}
        }
    }


    fun onPlayPauseToggle(){
        if (mPlayer == null){
            return
        }
        if (mPlayer!!.isPlaying()){
            mPlayer!!.pause()
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
        }else{
            mPlayer!!.play()
            ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
        }
    }

    private fun onPlayMusic(playListEvent: PlayListEvent) {

        val playList: PlayList = playListEvent.playlist
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwitchNext(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete(next: Music) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlayStatusChanged(isPlaying: Boolean) {
        updatePlayToggle(isPlaying)
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
        if (song == null){
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

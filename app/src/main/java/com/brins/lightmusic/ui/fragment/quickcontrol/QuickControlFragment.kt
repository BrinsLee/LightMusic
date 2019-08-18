package com.brins.lightmusic.ui.fragment.quickcontrol

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.event.PlayOnLineMusicEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.player.IPlayback
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.player.broadcast.HeadsetButtonReceiver
import com.brins.lightmusic.ui.activity.MusicPlayActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.getStringCover
import com.brins.lightmusic.utils.string2Bitmap
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_quick_control.*
import java.lang.ref.WeakReference


class QuickControlFragment : BaseFragment(), MusicPlayerContract.View, IPlayback.Callback, View.OnClickListener,
    HeadsetButtonReceiver.onHeadsetListener {

    var mPlayer: IPlayback? = null

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
        HeadsetButtonReceiver(this)
        RxBus.getInstance().subscribe(Any::class.java
            , Consumer {
                when (it) {
                    is PlayListEvent -> onPlayMusic(it)
                    is PlayOnLineMusicEvent -> onPlayOnlineMusic(it)
                }
            })
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

    private fun playOnlineSong(playList: PlayList, index: Int) {
        playList.setPlayMode(PlayMode.getDefault())
        mPlayer!!.play(playList, index)
        val song = playList.getCurrentSong()
        mPresenter.getOnLineCover(song!!.cover!!)
    }

    private fun onPlayOnlineMusic(onLineMusicEvent: PlayOnLineMusicEvent) {
        if (mPlayer!!.isPlaying()) {
            mPlayer!!.pause()
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
        }
        if (!::playList.isInitialized){
            playList = PlayList()
        }
        val index = playList.getPlayingIndex() + 1
        val wtf = WeakReference<OnlineMusic>(onLineMusicEvent.music)
        val music = WeakReference<Music>(Music(wtf.get()!!.nameMusic, wtf.get()!!.nameMusic, wtf.get()!!.artistBeans!![0].name, wtf.get()!!.al!!.name
            ,wtf.get()!!.al!!.picUrl , wtf.get()!!.fileUrl, wtf.get()!!.dt ))

        playList.addSong(music.get(), index)
        playOnlineSong(playList, index)
    }

    override fun onStart() {
        super.onStart()
        MusicPlayerPresenter.instance.setContext(activity!!).setView(this).subscribe()

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
        Log.d(TAG,"headSet_PlayOrPause")
        mPlayer?.pause()
    }

    override fun playNext() {
        Log.d(TAG, "headSet_PlayNext")
        if (playList.getNumOfSongs() == 1){
            return
        }
        onPlayNext()
    }

    override fun playPrevious() {
        Log.d(TAG,"headSet_PlayLast")
        if (playList.getNumOfSongs() == 1){
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
        if (::mPresenter.isInitialized) {
            mPresenter.unsubscribe()
        }
        super.onDestroyView()
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
                    MusicPlayActivity.startThisActivity((activity as AppCompatActivity), mPlayer!!.isPlaying())
                }
                return
            }
        }
    }

    /*fun disappear(){
        if(playBarLayout == null){
            return
        }
        playBarLayout.visibility = View.GONE
    }

    fun appear(){
        if (playBarLayout == null){
            return
        }
        if (playBarLayout.visibility == View.GONE)
        {
            playBarLayout.visibility = View.VISIBLE
        }else{
            return
        }
    }*/

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
    }

    fun onPlayNext() {
        if (mPlayer == null) return
        mPlayer!!.playNext()
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
    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    override fun onCoverLoad(cover: Bitmap?) {
        playList.getCurrentSong()!!.cover = getStringCover(cover)
        playList.getCurrentSong()!!.bitmapCover = cover
        onSongUpdated(playList.getCurrentSong())
    }

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
        mPlayer!!.unregisterCallback(this)
        mPlayer = null
    }

    override fun onSongSetAsFavorite(song: Music?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSongUpdated(song: Music?) {
        if (song == null) {
            ivPlayOrPause.setImageResource(R.drawable.ic_playmusic)
            return
        }
        tvPlaybarTitle.text = song.name
        tvPlaybarArtist.text = song.singer
        val bitmap = string2Bitmap(song.cover!!)
        song.bitmapCover = bitmap
        ivPlaybarCover.setImageBitmap(bitmap)
        ivPlayOrPause.setImageResource(R.drawable.ic_pausemusic)
        ivPlaybarCover.startRotateAnimation()

    }

}

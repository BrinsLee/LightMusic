package com.brins.lightmusic.player

import android.content.Context.AUDIO_SERVICE
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.NotificationUpadteEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.fragment.quickcontrol.MusicPlayerPresenter
import com.brins.lightmusic.utils.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

class Player : IPlayback, MediaPlayer.OnCompletionListener,
    AudioManager.OnAudioFocusChangeListener {

    companion object {
        private val TAG = "Player"
        @Volatile
        private var sInstance: Player? = null

        @JvmStatic
        fun getInstance(): Player {
            if (sInstance == null) {
                synchronized(Player::class.java) {
                    if (sInstance == null) {
                        sInstance = Player()
                    }
                }
            }
            return sInstance!!
        }
    }

    private val mAudioManager: AudioManager by lazy {
        LightMusicApplication.getLightApplication().getSystemService(
            AUDIO_SERVICE
        ) as AudioManager
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private val mAudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()

    @RequiresApi(Build.VERSION_CODES.O)
    private val mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(mAudioAttributes).setAcceptsDelayedFocusGain(true)
        .setOnAudioFocusChangeListener(this).build()
    private var mPlayer: MediaPlayer = MediaPlayer()
    private var mPlayList: PlayList = PlayList()
    private var mPlayOnAudioFocus = false
    // Default size 2: for service and UI
    private val mCallbacks = ArrayList<IPlayback.Callback>(1)

    // Player status
    private var isPaused: Boolean = false
    private var isPlayingBeforeLoseFocuse = true

    init {
        mPlayer.setOnCompletionListener(this)
        mPlayer.setWakeMode(
            LightMusicApplication.getLightApplication(),
            PowerManager.PARTIAL_WAKE_LOCK
        )
    }

    /**
     *
     * 音乐焦点改变
     */
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                mPlayOnAudioFocus = false
                isPlayingBeforeLoseFocuse = mPlayer.isPlaying
                pause()
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                mPlayOnAudioFocus = true
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest)
                } else {
                    mAudioManager.abandonAudioFocus(this)
                }
                stop()
            }
        }
    }


    /**
     * 请求焦点
     */
    private fun requestFocus(): Boolean {
        // Request audio focus for playback
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val result = mAudioManager.requestAudioFocus(mAudioFocusRequest)
            result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            val result = mAudioManager.requestAudioFocus(
                this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN
            )
            result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    /**
     * 设置播放列表
     */
    override fun setPlayList(list: PlayList) {
        mPlayList = list
    }

    override fun getPlayList(): PlayList {
        return mPlayList
    }

    override fun play(): Boolean {
        mPlayOnAudioFocus = requestFocus()
        //暂停后播放
        if (mPlayOnAudioFocus) {
            if (isPaused) {
                mPlayer.start()
                notifyPlayStatusChanged(true)
                return true
            }
            //播放新音乐
            if (mPlayList.prepare()) {
                var music = mPlayList.getCurrentSong()
                val url = music?.fileUrl
                if (url.isNullOrEmpty()) {
                    launch({
                        music = loadMusicDetail(music!!)
                        try {
                            mPlayer.reset()
                            mPlayer.setDataSource(music?.fileUrl)
                            mPlayer.prepare()
                            mPlayer.start()
                            notifyPlayStatusChanged(true, music)
                        } catch (e: Exception) {
                            Log.e(TAG, "play: ", e)
                            notifyPlayStatusChanged(false, music)
                        }

                    }, {
                        Toast.makeText(
                            BaseApplication.getInstance().baseContext,
                            "付费音乐，已自动播放下一首",
                            Toast.LENGTH_SHORT
                        ).show()
                        playNext()
                    })
                } else {
                    try {
                        mPlayer.reset()
                        mPlayer.setDataSource(music?.fileUrl)
                        mPlayer.prepare()
                        mPlayer.start()
                        notifyPlayStatusChanged(true, music)
                    } catch (e: Exception) {
                        Log.e(TAG, "play: ", e)
                        notifyPlayStatusChanged(false, music)
                        return false
                    }
                }
                return true
            }
        }
        return false
    }


    private suspend fun loadMusicDetail(onlineMusic: Music) = withContext(Dispatchers.IO) {
        val result = MusicPlayerPresenter.instance.loadMusicDetail(onlineMusic)
        result
    }

    private fun notifyPlayStatusChanged(isPlaying: Boolean, music: Music? = null) {
        RxBus.getInstance().post(NotificationUpadteEvent())
        for (callback in mCallbacks) {
            callback.onPlayStatusChanged(isPlaying, music)
        }
    }

    override fun play(startIndex: Int): Boolean {

        if (startIndex < 0 || startIndex >= mPlayList.getNumOfSongs()) {
            return false
        }
        isPaused = false
        mPlayList.setPlayingIndex(startIndex)
        return play()
    }

    override fun play(song: Music): Boolean {
        isPaused = false
        mPlayList.getSongs().clear()
        mPlayList.getSongs().add(song)
        return play()
    }

    override fun playLast(): Boolean {

        isPaused = false
        mPlayList.last()
        play()
        return true
    }


    override fun playNext(): Boolean {
        isPaused = false
        mPlayList.next()
        play()
        return true
    }


    //focus true isplaying false
    override fun pause(): Boolean {
        Log.d(TAG, "$isPlayingBeforeLoseFocuse")
        Log.d(TAG, "$mPlayOnAudioFocus")

        if (mPlayOnAudioFocus && !mPlayer.isPlaying && isPlayingBeforeLoseFocuse) {
            mPlayer.start()
            return false
        } else if (mPlayer.isPlaying) {
            mPlayer.pause()
            isPaused = true
            notifyPlayStatusChanged(false)
            return true
        }
        return false
    }

    override fun stop() {
        mPlayer.stop()
    }

    override fun isPlaying(): Boolean {

        var isPlay = false
        try {
            isPlay = mPlayer.isPlaying
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isPlay
    }

    override fun getProgress(): Int {
        return mPlayer.currentPosition
    }

    override fun getPlayingSong(): Music? {
        return mPlayList.getCurrentSong()
    }

    override fun seekTo(progress: Int): Boolean {

        if (mPlayList.getSongs().isEmpty()) return false
        val currentSong = mPlayList.getCurrentSong()
        if (currentSong != null) {
            if (currentSong.duration <= progress) {
                onCompletion(mPlayer)
            } else {
                mPlayer.seekTo(progress)
            }
            return true
        }
        return false
    }

    override fun setPlayMode(playMode: PlayMode) {
        mPlayList.setPlayMode(playMode)
    }

    override fun registerCallback(callback: IPlayback.Callback) {
        mCallbacks.add(callback)
    }

    override fun unregisterCallback(callback: IPlayback.Callback) {
        mCallbacks.remove(callback)
    }

    override fun removeCallbacks() {
        mCallbacks.clear()
    }

    override fun releasePlayer() {
        mPlayer.reset()
        mPlayer.release()
        sInstance = null
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (mPlayList.getPlayMode() === PlayMode.LIST && mPlayList.getPlayingIndex() === mPlayList.getNumOfSongs() - 1) run {
            // In the end of the list
            // Do nothing, just deliver the callback
        } else if (mPlayList.getPlayMode() === PlayMode.SINGLE) {
            play()
        } else {
            val hasNext = mPlayList.hasNext(true)
            if (hasNext) {
                playNext()
            }
        }
    }


}
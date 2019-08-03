package com.brins.lightmusic.player

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.media.AudioManager
import android.content.IntentFilter




class MediaSessionManager(private val mPlayService : PlayBackService) {

    private val mNoisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private val broard = NoisyAudioStreamReceiver()
    val mMediaSession : MediaSession by lazy { MediaSession(mPlayService, Tag) }
    val callback = object :MediaSession.Callback(){
        override fun onPlay() {
            mPlayService.play()
        }

        override fun onPause() {
            mPlayService.pause()
        }

        override fun onSkipToPrevious() {
            mPlayService.playLast()
        }

        override fun onSkipToNext() {
            mPlayService.playNext()
        }

    }
    companion object{
        @JvmStatic
        val Tag = "MediaSessionManager"

        private val MEDIA_SESSION_ACTIONS = (
                PlaybackState.ACTION_PLAY
                        or PlaybackState.ACTION_PAUSE
                        or PlaybackState.ACTION_PLAY_PAUSE
                        or PlaybackState.ACTION_SKIP_TO_NEXT
                        or PlaybackState.ACTION_SKIP_TO_PREVIOUS
                        or PlaybackState.ACTION_STOP
                        or PlaybackState.ACTION_SEEK_TO)
    }

    init {
        setupMediaSession()
        mPlayService.registerReceiver(broard,mNoisyFilter)
    }

    @SuppressLint("WrongConstant")
    private fun setupMediaSession() {
//        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSession.FLAG_HANDLES_MEDIA_BUTTONS)
        mMediaSession.setCallback(callback)
        mMediaSession.isActive = true
    }


    fun release() {
        mMediaSession.setCallback(null)
        mMediaSession.isActive = false
        mMediaSession.release()
        mPlayService.unregisterReceiver(broard)
    }

   inner class NoisyAudioStreamReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            mPlayService.pause()
        }

    }
}
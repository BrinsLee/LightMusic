package com.brins.lightmusic.player

import android.annotation.SuppressLint
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaSessionManager(private val mPlayService : PlayBackService) {

    val mMediaSession : MediaSessionCompat by lazy { MediaSessionCompat(mPlayService, Tag) }
    val callback = object :MediaSessionCompat.Callback(){
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
                PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        or PlaybackStateCompat.ACTION_STOP
                        or PlaybackStateCompat.ACTION_SEEK_TO)
    }

    init {
        setupMediaSession()
    }

    @SuppressLint("WrongConstant")
    private fun setupMediaSession() {
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS)
        mMediaSession.setCallback(callback)
        mMediaSession.isActive = true
    }


    fun release() {
        mMediaSession.setCallback(null)
        mMediaSession.isActive = false
        mMediaSession.release()
    }

}
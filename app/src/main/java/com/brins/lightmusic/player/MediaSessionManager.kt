package com.brins.lightmusic.player

import android.annotation.SuppressLint
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaSessionManager(private val mPlayService : PlayBackService) {

    val mMediaSession : MediaSession by lazy { MediaSession(mPlayService, Tag) }
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
//        mMediaSession.setCallback()
        mMediaSession.isActive = true
    }

}
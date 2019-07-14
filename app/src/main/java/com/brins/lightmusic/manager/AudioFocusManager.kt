package com.brins.lightmusic.manager

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager

class AudioFocusManager(var context : Context) : AudioManager.OnAudioFocusChangeListener {

    private val TAG = "AudioFocusManager"
    private val isPausedByFocusLossTransient: Boolean = false

    private val audioManager : AudioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    override fun onAudioFocusChange(focusChange: Int) {
        when(focusChange){
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (isPausedByFocusLossTransient){

                }
            }
        }

    }

    fun requestAudioFocus() : Boolean{
        return audioManager.requestAudioFocus(this
            ,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    fun abondonAudioFocus(){
        audioManager.abandonAudioFocus(this)
    }
}
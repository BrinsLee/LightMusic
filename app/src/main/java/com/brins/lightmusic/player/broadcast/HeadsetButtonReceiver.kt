package com.brins.lightmusic.player.broadcast

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import java.util.*
import android.content.ComponentName
import android.content.pm.PackageManager
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver
import com.brins.lightmusic.BaseApplication


class HeadsetButtonReceiver(head: onHeadsetListener) : MediaButtonReceiver() {


    private val context: Context by lazy { BaseApplication.getInstance().applicationContext }
    private var timer = Timer()

    companion object {
        private val PLAY_OR_PAUSE = 1
        private val PLAY_NEXT = 2
        private val PLAY_PREVIOUS = 3
        private var clickCount: Int = 0
        private lateinit var headesetListener: onHeadsetListener
        private lateinit var mMediaSession: MediaSessionCompat

        class HeadsetTimerTask : TimerTask() {
            override fun run() {
                when (clickCount) {
                    1 -> handler.sendEmptyMessage(PLAY_OR_PAUSE)
                    2 -> handler.sendEmptyMessage(PLAY_NEXT)
                    3 -> handler.sendEmptyMessage(PLAY_PREVIOUS)
                }
                clickCount = 0
            }

        }

        var handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                try {
                    when (msg.what) {
                        PLAY_OR_PAUSE -> headesetListener.playOrPause()
                        PLAY_NEXT -> headesetListener.playNextSong()
                        PLAY_PREVIOUS -> headesetListener.playPreviousSong()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    init {
        headesetListener = head
        registerHeadsetReceiver()
    }

    private fun registerHeadsetReceiver() {
        val name = ComponentName(context.packageName, HeadsetButtonReceiver::class.java.name)
        context.packageManager.setComponentEnabledSetting(
            name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            , PackageManager.DONT_KILL_APP
        )
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.component = name
        val pending = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        mMediaSession = MediaSessionCompat(context, "mbr", name, null)
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mMediaSession.setMediaButtonReceiver(pending)
        val state = PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_FAST_FORWARD or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP
        ).build()
        mMediaSession.setPlaybackState(state)
        setListener()
        //把MediaSession置为active，这样才能开始接收各种信息
        if (!mMediaSession.isActive) {
            mMediaSession.isActive = true
        }
    }

    private fun setListener() {
        mMediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(intent: Intent): Boolean {
                //通过Callback返回按键信息，为复用MediaButtonReceiver，直接调用它的onReceive()方法

                if (Intent.ACTION_MEDIA_BUTTON == intent.action) {
                    val keyEvent: KeyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                    if (keyEvent.action == KeyEvent.ACTION_UP) {
                        clickCount += 1
                        when (keyEvent.keyCode) {
                            KeyEvent.KEYCODE_HEADSETHOOK -> {
                                val headsetTimerTask = HeadsetTimerTask()
                                timer.schedule(headsetTimerTask, 500)
                            }
                            KeyEvent.KEYCODE_MEDIA_PAUSE, KeyEvent.KEYCODE_MEDIA_PLAY -> handler.sendEmptyMessage(PLAY_OR_PAUSE)
                            KeyEvent.KEYCODE_MEDIA_NEXT -> handler.sendEmptyMessage(PLAY_NEXT)
                            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> handler.sendEmptyMessage(PLAY_PREVIOUS)
                        }
                    }
                }
                return true
            }
        })
    }

    override fun onReceive(context: Context?, intent: Intent) {
    }


    interface onHeadsetListener {
        fun playOrPause()
        fun playNextSong()
        fun playPreviousSong()
    }
}
package com.brins.lightmusic.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import java.lang.Exception
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.common.AppConfig.*
import com.brins.lightmusic.event.NotificationUpadteEvent
import com.brins.lightmusic.player.broadcast.HeadsetButtonReceiver
import com.brins.lightmusic.ui.activity.SplashActivity
import com.hwangjr.rxbus.annotation.Subscribe


class PlayBackService : Service(), IPlayback, HeadsetButtonReceiver.onHeadsetListener {


    companion object {
        @JvmStatic
        private val CHANNEL_ID = "com.brins.lightmusic.notification.channel"
        private val NOTIFICATION_ID = 1
        var mIsServiceBound: Boolean = false
    }

    private val mPlayer: Player by lazy { Player.getInstance() }
    private val mBinder = LocalBinder()
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var remoteView: RemoteViews


    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class LocalBinder : Binder() {
        val service: PlayBackService
            get() = this@PlayBackService
    }

    override fun onCreate() {
        super.onCreate()
        HeadsetButtonReceiver(this)
        mIsServiceBound = true
        MediaSessionManager(this)
        RxBus.getInstance().register(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateNotification()
        }
        cancelNotification()
        val filter = IntentFilter()
        filter.addAction(ACTION_PLAY)
        filter.addAction(ACTION_PAUSE)
        filter.addAction(ACTION_NEXT)
        filter.addAction(ACTION_PRV)
        registerReceiver(playMusicReceiver, filter)
    }

    override fun stopService(name: Intent): Boolean {
        stopForeground(true)
        return super.stopService(name)
    }

    override fun onDestroy() {
        releasePlayer()
        RxBus.getInstance().unregister(this)
        unregisterReceiver(playMusicReceiver)
        try {
            val intent = Intent(applicationContext, PlayBackService::class.java)
            startService(intent)
        } catch (e: Exception) {

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            val intent = Intent(this, SplashActivity::class.java)
            val intentGo =
                PendingIntent.getActivity(
                    this,
                    CODE_MAIN,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            remoteView.setOnClickPendingIntent(R.id.notice, intentGo)
            val intentClose =
                PendingIntent.getActivity(
                    this,
                    CODE_CLOSE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            remoteView.setOnClickPendingIntent(R.id.widget_close, intentClose)
            val prv = Intent()
            prv.action = ACTION_PRV
            val intent_prv =
                PendingIntent.getBroadcast(this, CODE_PRV, prv, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.widget_prev, intent_prv)
            //正在播放，暂停
            if (isPlaying()) {
                val playorpause = Intent()
                playorpause.action = ACTION_PAUSE
                val intent_play = PendingIntent.getBroadcast(
                    this,
                    CODE_PAUSE,
                    playorpause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play)
            }
            if (!isPlaying()) {
                val playorpause = Intent()
                playorpause.action = ACTION_PLAY
                val intent_play = PendingIntent.getBroadcast(
                    this,
                    CODE_PLAY,
                    playorpause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play)

            }
            val next = Intent()
            next.action = ACTION_NEXT
            val intent_next =
                PendingIntent.getBroadcast(this, CODE_NEXT, next, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.widget_next, intent_next)
            builder.setCustomContentView(remoteView)
                .setSmallIcon(R.drawable.ic_music_note_white_48dp)
                .setContentTitle("轻籁")
            mNotificationManager = getSystemService(NotificationManager::class.java)
            mNotificationManager.createNotificationChannel(channel)
            startForeground(NOTIFICATION_ID, builder.build())

        }
    }

    private fun cancelNotification() {
        mNotificationManager.cancelAll() //从状态栏中移除通知
    }

    override fun playOrPause() {
        pause()
    }

    override fun playPreviousSong() {
        playLast()
    }

    override fun playNextSong() {
        playNext()
    }

    fun playMusic() {
        if (getPlayList().getNumOfSongs() == 0) {
            return
        }
        val playList = getPlayList()
        playList.setPlayMode(PlayMode.getDefault())

    }


    //IPlayback
    override fun setPlayList(list: PlayList) {
        mPlayer.setPlayList(list)
    }

    override fun getPlayList(): PlayList {
        return mPlayer.getPlayList()
    }

    override fun play(): Boolean {
        return mPlayer.play()
    }

    override fun play(startIndex: Int): Boolean {
        return mPlayer.play(startIndex)
    }

    override fun play(song: Music): Boolean {
        return mPlayer.play(song)
    }

    override fun playLast(): Boolean {
        return mPlayer.playLast()
    }

    override fun playNext(): Boolean {
        return mPlayer.playNext()
    }

    override fun pause(): Boolean {
        return mPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mPlayer.isPlaying()
    }

    override fun getProgress(): Int {
        return mPlayer.getProgress()
    }

    override fun getPlayingSong(): Music? {
        return mPlayer.getPlayingSong()
    }

    override fun seekTo(progress: Int): Boolean {
        return mPlayer.seekTo(progress)
    }

    override fun setPlayMode(playMode: PlayMode) {

        mPlayer.setPlayMode(playMode)
    }

    override fun registerCallback(callback: IPlayback.Callback) {
        mPlayer.registerCallback(callback)
    }

    override fun unregisterCallback(callback: IPlayback.Callback) {
        mPlayer.unregisterCallback(callback)
    }

    override fun removeCallbacks() {
        mPlayer.removeCallbacks()
    }

    override fun releasePlayer() {
        mPlayer.releasePlayer()
        super.onDestroy()
    }

    override fun stop() {
        mPlayer.stop()
    }


    private val playMusicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (!action.isNullOrEmpty()) {
                when (action) {
                    ACTION_PRV -> playLast()
                    ACTION_NEXT -> playNext()
                    ACTION_PAUSE -> {
                        pause()
                        updateNotification()
                    }
                    ACTION_PLAY -> play()
                }
            }
        }

    }

    private fun updateNotification() {
        remoteView = RemoteViews(packageName, R.layout.view_notification)
        if (getPlayList().getCurrentSong()?.bitmapCover == null)
            remoteView.setImageViewResource(
                R.id.widget_album,
                R.drawable.default_cover
            )
        else remoteView.setImageViewBitmap(
            R.id.widget_album,
            getPlayList().getCurrentSong()?.bitmapCover
        )
        remoteView.setTextViewText(
            R.id.widget_title,
            if (getPlayList().getCurrentSong()?.name.isNullOrEmpty()) "无音乐" else getPlayList().getCurrentSong()?.name
        )
        val artist = getPlayList().getCurrentSong()?.artistBeans
        remoteView.setTextViewText(
            R.id.widget_artist,
            if (artist.isNullOrEmpty()) "未知"
            else getPlayList().getCurrentSong()?.artistBeans!![0].name
        )
        if (isPlaying()) {
            remoteView.setImageViewResource(R.id.widget_play, R.drawable.ic_play_bar_btn_pause)
        } else {
            remoteView.setImageViewResource(R.id.widget_play, R.drawable.ic_play_bar_btn_play)
        }
        createNotificationChannel()
    }

    @Subscribe
    fun onPlayMusic(updateEvent: NotificationUpadteEvent) {
        updateNotification()
    }
}

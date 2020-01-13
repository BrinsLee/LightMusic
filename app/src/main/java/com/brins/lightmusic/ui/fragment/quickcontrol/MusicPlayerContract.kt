package com.brins.lightmusic.ui.fragment.quickcontrol

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.player.PlayBackService
import com.brins.lightmusic.player.PlayMode
import com.brins.lightmusic.ui.base.BasePresenter
import com.brins.lightmusic.ui.base.BaseView

interface MusicPlayerContract {

    interface View : BaseView {

        fun onPlaybackServiceBound(service: PlayBackService)

        fun onPlaybackServiceUnbound()

        fun onSongSetAsFavorite(@NonNull song: Music?)

        fun onSongUpdated(@Nullable song: Music?)

        fun updatePlayMode(playMode: PlayMode)

        fun updatePlayToggle(play: Boolean)

        fun updateFavoriteToggle(favorite: Boolean)


//        fun onMusicDetail(onlineMusic: Music)
    }

    interface Presenter : BasePresenter<View> {
        /**
         * 检索上次播放模式
         */
        fun retrieveLastPlayMode()
        /** 喜欢音乐
         * @param song 选择的音乐
         * @param favorite 添加或移除收藏
         */
        fun setSongAsFavorite(song: Music, favorite: Boolean)
        /**
         *绑定播放音乐服务
         */
        fun bindPlaybackService()
        /**
         * 解除绑定
         */
        fun unbindPlaybackService()



        suspend fun loadMusicDetail(onlineMusic: Music) : Music
    }
}
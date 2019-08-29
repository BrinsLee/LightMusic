package com.brins.lightmusic.api

import com.brins.lightmusic.api.service.ArtistService
import com.brins.lightmusic.api.service.MusicService
import com.brins.lightmusic.api.service.MusicVideoService
import com.brins.lightmusic.api.service.PlayListService
import com.brins.lightmusic.common.AppConfig.BASEURL

object ApiHelper {

    val LOG_TAG_NETWORK_LOGIN = "network_login"
    val LOG_TAG_NETWORK_USERINFO = "network_userinfo"
    val LOG_TAG_NETWORK_API = "network_api"

    private var mMusicService: MusicService? = null
    private var mPlayListService: PlayListService? = null
    private var mArtistService: ArtistService? = null
    private var mMvService : MusicVideoService? = null

    fun getMusicSerVice(): MusicService{
        if (mMusicService == null){
            synchronized(MusicService::class.java){
                if (mMusicService == null){
                    mMusicService = RetrofitFactory.newRetrofit(BASEURL).create(MusicService::class.java)
                }
            }
        }
        return mMusicService!!
    }

    fun getPlayListSerVice(): PlayListService{
        if (mPlayListService == null){
            synchronized(PlayListService::class.java){
                if (mPlayListService == null){
                    mPlayListService = RetrofitFactory.newRetrofit(BASEURL).create(PlayListService::class.java)
                }
            }
        }
        return mPlayListService!!
    }

    fun getArtistSerVice(): ArtistService{
        if (mArtistService == null){
            synchronized(ArtistService::class.java){
                if (mArtistService == null){
                    mArtistService = RetrofitFactory.newRetrofit(BASEURL).create(ArtistService::class.java)
                }
            }
        }
        return mArtistService!!
    }

    fun getMvSerVice(): MusicVideoService{
        if (mMvService == null){
            synchronized(MusicVideoService::class.java){
                if (mMvService == null){
                    mMvService = RetrofitFactory.newRetrofit(BASEURL).create(MusicVideoService::class.java)
                }
            }
        }
        return mMvService!!
    }
}
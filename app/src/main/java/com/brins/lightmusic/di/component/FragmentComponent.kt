package com.brins.lightmusic.di.component

import android.app.Activity
import com.brins.lightmusic.di.module.FragmentModule
import com.brins.lightmusic.di.scope.FragmentScope
import com.brins.lightmusic.ui.fragment.artists.ArtistCatgoryFragment
import com.brins.lightmusic.ui.fragment.artists.ArtistDetailFragment
import com.brins.lightmusic.ui.fragment.artists.ArtistFragment
import com.brins.lightmusic.ui.fragment.artists.ArtistTabFragment
import com.brins.lightmusic.ui.fragment.dailyrecommend.DailyRecommendFragment
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryFragment
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.ui.fragment.localmusic.LocalMusicFragment
import com.brins.lightmusic.ui.fragment.myfragment.MyFragment
import com.brins.lightmusic.ui.fragment.quickcontrol.QuickControlFragment
import com.brins.lightmusic.ui.fragment.search.SearchFragment
import com.brins.lightmusic.ui.fragment.usermusiclist.UserMusicListFragment
import com.brins.lightmusic.ui.fragment.video.VideoCategoryFragment
import com.brins.lightmusic.ui.fragment.video.VideoDetailFragment
import com.brins.lightmusic.ui.fragment.video.VideoFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun getActivity(): Activity

    fun inject(artistCatgoryFragment: ArtistCatgoryFragment)
    fun inject(artistCatgoryFragment: ArtistDetailFragment)
    fun inject(artistFragment: ArtistFragment)
    fun inject(artistTabFragment: ArtistTabFragment)
    fun inject(dailyRecommendFragment: DailyRecommendFragment)
    fun inject(discoveryFragment: DiscoveryFragment)
    fun inject(localMusicFragment: LocalMusicFragment)
    fun inject(musicDetailFragment: MusicDetailFragment)
    fun inject(myFragment: MyFragment)
    fun inject(quickControlFragment: QuickControlFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(userMusicListFragment: UserMusicListFragment)
    fun inject(videoCategoryFragment: VideoCategoryFragment)
    fun inject(videoDetailFragment: VideoDetailFragment)
    fun inject(videoFragment: VideoFragment)


}
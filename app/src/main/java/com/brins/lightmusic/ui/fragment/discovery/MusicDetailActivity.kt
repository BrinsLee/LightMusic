package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.model.PlayListDetail
import com.brins.lightmusic.ui.base.BaseActivity

class MusicDetailActivity : BaseActivity(), DiscoveryContract.View{

    lateinit var mPresenter: DiscoveryContract.Presenter
    val id : String by lazy {intent.getStringExtra("MusicID")}

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        DiscoverPresent(this).loadMusicListDetail(id)
    }

    //MVP View

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getcontext(): Context {
        return this
    }

    override fun getLifeActivity(): AppCompatActivity {
        return this
    }

    override fun handleError(error: Throwable) {
    }

    override fun onMusicListLoad(songs: MutableList<MusicList>) {
    }

    override fun onArtistLoad(artists: MutableList<Artist>) {
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
        mPresenter = presenter!!
    }

    override fun onDetailLoad(detail: PlayListDetail) {

    }
}

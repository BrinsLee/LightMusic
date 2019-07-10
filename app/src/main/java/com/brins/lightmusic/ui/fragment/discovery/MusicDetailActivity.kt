package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.model.PlayListDetail
import com.brins.lightmusic.ui.base.BaseActivity
import kotlinx.android.synthetic.main.include_loading_animation.*

class MusicDetailActivity : BaseActivity(), DiscoveryContract.View {

    companion object {
        @JvmStatic
        val MUSIC_ID = "musicId"

        @JvmStatic
        fun startThisActivity(activity: AppCompatActivity, id: String) {
            val intent = Intent(activity, MusicDetailActivity::class.java)
            intent.putExtra(MUSIC_ID, id)
            activity.startActivity(intent)
        }
    }

    lateinit var mPresenter: DiscoveryContract.Presenter
    var id: String = ""

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        id = intent.getStringExtra(MUSIC_ID)
        DiscoverPresent(this).loadMusicListDetail(id)
    }

    //MVP View

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE

    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
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

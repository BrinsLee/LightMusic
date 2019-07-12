package com.brins.lightmusic.ui.fragment.discovery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.LightMusicApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.model.OnlineMusic
import com.brins.lightmusic.model.PlayListDetail
import com.brins.lightmusic.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_music_detail.*
import kotlinx.android.synthetic.main.include_loading_animation.*

class MusicDetailActivity : BaseActivity(), DiscoveryContract.View {

    companion object {
        @JvmStatic
        val MUSIC_ID = "musicId"

        @JvmStatic
        fun startThisActivity(activity: AppCompatActivity, id: String , options : Bundle) {
            val intent = Intent(activity, MusicDetailActivity::class.java)
            intent.putExtra(MUSIC_ID, id)
            activity.startActivity(intent , options)
            activity.finish()
        }
    }

    private val mApplication = LightMusicApplication.getLightApplication()
    private val proxy : HttpProxyCacheServer by lazy { mApplication.getProxy(this) }
    lateinit var mPresenter: DiscoveryContract.Presenter
    var id: String = ""
    var musicDetails = mutableListOf<OnlineMusic>()
    lateinit var mAdapter: MusicDetailAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        id = intent.getStringExtra(MUSIC_ID)
        setSupportActionBar(toolbar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            if (p1 == 0){
                playAll.show()
            }else{
                playAll.hide()
            }
        })
        mAdapter = MusicDetailAdapter(this, musicDetails)
        DiscoverPresent(this).loadMusicListDetail(id)
        musicRecycler.adapter = mAdapter
        musicRecycler.layoutManager = LinearLayoutManager(this)
        musicRecycler.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )

    }

    override fun onStart() {
        super.onStart()
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
        collapsing.title = detail.name
        mAdapter.setData(detail.tracks as MutableList<OnlineMusic>)
        mAdapter.notifyDataSetChanged()
    }

}

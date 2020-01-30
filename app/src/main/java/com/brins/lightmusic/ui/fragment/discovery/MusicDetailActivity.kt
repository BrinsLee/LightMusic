package com.brins.lightmusic.ui.fragment.discovery

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.ui.fragment.usermusiclist.MusicListPresenter
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_music_detail.*
import kotlinx.android.synthetic.main.fragment_music_detail.*
import kotlinx.android.synthetic.main.fragment_music_detail.coverMusicList
import kotlinx.android.synthetic.main.fragment_music_detail.musicRecycler
import kotlinx.android.synthetic.main.fragment_music_detail.nestScrollView
import kotlinx.android.synthetic.main.fragment_music_detail.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicDetailActivity : BaseActivity(), DiscoveryContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {

    @Inject
    lateinit var mPresenter: DiscoverPresenter
    var id: String = ""
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()
    private var currentTime: Long = 0

    companion object {
        fun startThis(activity: AppCompatActivity, TAG: String, id: String) {
            val bundle = Intent(activity, MusicDetailActivity::class.java)
            bundle.putExtra(TAG, id)
            activity.startActivity(bundle)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    override fun initInject() {
        getActivityComponent().inject(this)
    }

    override fun handleError(error: Throwable) {
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        setTranslucent(this)
        mAdapter = MusicDetailAdapter(this, playList.getSongs())
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        nestScrollView.fadingView = toolbar
        nestScrollView.fadingHeightView = coverMusicList
        DiscoverPresenter.instance.subscribe(this)
        if (intent != null) {
            id = intent.getStringExtra("DiscoveryFragment")
            if (id === "") {
                id = intent.getStringExtra("ArtistTabFragment")
                loadAlbumDetail()
            } else {
                loadMusicListDetail()
            }
            musicRecycler.adapter = mAdapter
            musicRecycler.layoutManager = LinearLayoutManager(this)
            musicRecycler.addItemDecoration(
                SpacesItemDecoration(this, 2, R.color.gery)
            )
        }
    }


    private fun loadMusicListDetail() {
        launch({
            showLoading()
            val musicData = getMusicListData()
            val detailBean = musicData.playlist
            if (detailBean != null) {
                ImageLoadreUtils.getInstance().loadImage(
                    this,
                    ImageLoader.Builder().url(detailBean.coverImgUrl).assignWidth(640).assignHeight(480)
                    .bulid(), object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val result = rsBlur(this@MusicDetailActivity, resource, 25f)
                            coverBg.setImageBitmap(result)
                        }

                    }
                )
                Glide.with(this)
                    .load(detailBean.coverImgUrl)
                    .into(coverMusicList)
                toolbar.title = detailBean.name
                playList.addSong(detailBean.tracks!!)
                mAdapter.setData(playList.getSongs())
                mAdapter.notifyDataSetChanged()
                hideLoading()
            }

        }, {})
    }

    private fun loadAlbumDetail() {
        launch({
            showLoading()
            val musics = getAlbumData()
            Glide.with(this)
                .load(musics.album!!.picUrl)
                .into(coverMusicList)
            toolbar.title = musics.album!!.name
            playList.addSong(musics.songs!!)
            mAdapter.setData(playList.getSongs())
            mAdapter.notifyDataSetChanged()
            hideLoading()
        }, {
            val i = it
            Toast.makeText(this, i.message, Toast.LENGTH_SHORT).show()
            hideLoading()

        })
    }


    private suspend fun getMusicListData() = withContext(Dispatchers.IO) {
        val musicList = mPresenter.loadMusicListDetail(id)
        musicList
    }

    private suspend fun getAlbumData() = withContext(Dispatchers.IO) {
        val albumList = mPresenter.loadAlbumDetail(id)
        albumList
    }


    override fun onItemClick(view: View?, position: Int) {

        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        playList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
    }

    override fun onBackClick(view: View) {
        finish()
    }

}

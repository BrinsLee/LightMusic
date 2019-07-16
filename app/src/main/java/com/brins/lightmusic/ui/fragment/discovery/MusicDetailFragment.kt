package com.brins.lightmusic.ui.fragment.discovery


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.brins.lightmusic.R
import com.brins.lightmusic.model.*
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_music_detail.*
import android.media.MediaPlayer
import java.lang.Exception


class MusicDetailFragment : BaseFragment(), DiscoveryContract.View {

    private val mPresenter: DiscoveryContract.Presenter by lazy { DiscoverPresent(this) }
    var id: String = ""
    var musicDetails = mutableListOf<OnlineMusic>()
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }

    companion object {
        var Instance: MusicDetailFragment? = null

        @JvmStatic
        fun newInstance(): MusicDetailFragment {
            return if (Instance == null) {
                Instance = MusicDetailFragment()
                Instance!!
            } else {
                Instance!!
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            if (p1 == 0) {
                playAll.show()
            } else {
                playAll.hide()
            }
        })
        mAdapter = MusicDetailAdapter(context!!, musicDetails)
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                mPresenter.loadMusicDetail(musicDetails[position].id)
            }
        })
        id = (activity as MainActivity).currentMusicListId
        mPresenter.loadMusicListDetail(id)
        musicRecycler.adapter = mAdapter
        musicRecycler.layoutManager = LinearLayoutManager(context)
        musicRecycler.addItemDecoration(
            DividerItemDecoration(
                context!!, LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun getcontext(): Context {
        return context!!
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    override fun handleError(error: Throwable) {
    }

    override fun onMusicListLoad(songs: MutableList<MusicList>) {
    }

    override fun onArtistLoad(artists: MutableList<Artist>) {
    }

    override fun onDetailLoad(detail: PlayListDetail) {
        Glide.with(context!!)
            .load(detail.coverImgUrl)
            .into(coverMusicList)
        collapsing.title = detail.name
        musicDetails = detail.tracks as MutableList<OnlineMusic>
        mAdapter.setData(detail.tracks as MutableList<OnlineMusic>)
        mAdapter.notifyDataSetChanged()
    }

    override fun onMusicDetail(metaData: MusicMetaData?) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(metaData!!.url)
        try {
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                hideLoading()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
    }
}

package com.brins.lightmusic.ui.fragment.discovery


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.brins.lightmusic.R
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_music_detail.*
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayOnLineMusicEvent
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic


class MusicDetailFragment : BaseFragment(), DiscoveryContract.View, OnItemClickListener {

    private val mPresenter: DiscoveryContract.Presenter by lazy { DiscoverPresent(this) }
    var id: String = ""
    var musicDetails = mutableListOf<OnlineMusic>()
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList =
        PlayList()

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }

    companion object {

        val Instance = SingletonHolder.holder
        private object SingletonHolder {
            val holder = MusicDetailFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, p1 ->
            if (p1 == 0) {
                playAll.show()
            } else {
                playAll.hide()
            }
        })
        mAdapter = MusicDetailAdapter(context!!, musicDetails)
        mAdapter.setOnItemClickListener(this)
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

    //ItemClick
    override fun onItemClick(position: Int) {
        mPresenter.loadMusicDetail(musicDetails[position])
    }


    //MVP VIEW
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

    override fun onMusicListLoad(songs: MutableList<MusicListBean>) {
    }

    override fun onArtistLoad(artistBeans: MutableList<ArtistBean>) {
    }

    override fun onDetailLoad(detailBean: MusicListDetailBean) {
        Glide.with(context!!)
            .load(detailBean.coverImgUrl)
            .into(coverMusicList)
        collapsing.title = detailBean.name
        musicDetails.addAll(detailBean.tracks!!)
        mAdapter.setData(musicDetails)
        mAdapter.notifyDataSetChanged()
    }

    override fun onMusicDetail(onlineMusic : OnlineMusic) {
        RxBus.getInstance().post(PlayOnLineMusicEvent(onlineMusic))
        hideLoading()
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
    }
}

package com.brins.lightmusic.ui.fragment.discovery


import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.brins.lightmusic.R
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.model.OnlineMusic
import com.brins.lightmusic.model.PlayListDetail
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_music_detail.*

class MusicDetailFragment : BaseFragment() , DiscoveryContract.View {


    lateinit var mPresenter: DiscoveryContract.Presenter
    var id: String = ""
    var musicDetails = mutableListOf<OnlineMusic>()
    lateinit var mAdapter: MusicDetailAdapter
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
            if (p1 == 0){
                playAll.show()
            }else{
                playAll.hide()
            }
        })
        mAdapter = MusicDetailAdapter(context!!, musicDetails)
        id = (activity as MainActivity).currentMusicListId
        DiscoverPresent(this).loadMusicListDetail(id)
        musicRecycler.adapter = mAdapter
        musicRecycler.layoutManager = LinearLayoutManager(context)
        musicRecycler.addItemDecoration(
            DividerItemDecoration(
                context!!, LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
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
        mAdapter.setData(detail.tracks as MutableList<OnlineMusic>)
        mAdapter.notifyDataSetChanged()
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
        mPresenter = presenter!!
    }
}

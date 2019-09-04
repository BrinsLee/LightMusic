package com.brins.lightmusic.ui.fragment.discovery


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_music_detail.*
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayOnLineMusicEvent
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.model.onlinemusic.MusicListDetailBean
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.customview.CommonHeaderView


class MusicDetailFragment : BaseFragment(), DiscoveryContract.View, OnItemClickListener , CommonHeaderView.OnBackClickListener {

    private lateinit var mPresenter: DiscoveryContract.Presenter
    var id: String = ""
    var musicDetails = mutableListOf<OnlineMusic>()
    lateinit var mAdapter: MusicDetailAdapter
    private var playList: PlayList = PlayList()

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MusicDetailAdapter(context!!, musicDetails)
        mAdapter.setOnItemClickListener(this)
        toolbar.setOnBackClickListener(this)
        nestScrollView.fadingView = toolbar
        nestScrollView.fadingHeightView = coverMusicList
        id = (activity as MainActivity).currentMusicListId
        DiscoverPresent.instance.subscribe(this)
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

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()
    }



    //MVP VIEW
    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

/*    override fun getcontext(): Context {
        return context!!
    }*/

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    override fun handleError(error: Throwable) {
    }

    override fun onMusicListLoad(songs: MutableList<MusicListBean>) {
    }

    override fun onBannerLoad(banners: ArrayList<Banner>) {
    }

    override fun onDetailLoad(detailBean: MusicListDetailBean) {
        Glide.with(context!!)
            .load(detailBean.coverImgUrl)
            .into(coverMusicList)
        toolbar.title = detailBean.name
        musicDetails.addAll(detailBean.tracks!!)
        mAdapter.setData(musicDetails)
        mAdapter.notifyDataSetChanged()
    }

    override fun onMusicDetail(onlineMusic : OnlineMusic) {
        RxBus.getInstance().post(PlayOnLineMusicEvent(onlineMusic))
        hideLoading()
    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter) {
        mPresenter = presenter
    }
}

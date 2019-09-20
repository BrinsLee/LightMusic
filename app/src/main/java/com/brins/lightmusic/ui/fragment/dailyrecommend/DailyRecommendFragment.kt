package com.brins.lightmusic.ui.fragment.dailyrecommend

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.dailyrecommend.DailyRecommendResult
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.AppBarStateChangeListener
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.SpacesItemDecoration
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.dp2px
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_daily_recommend.*

class DailyRecommendFragment : BaseFragment<DailyContract.Presenter>(),
    DailyContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {

    private var mPlayList = PlayList()
    private var currentTime: Long = 0
    private lateinit var mPresenter: DailyContract.Presenter
    private lateinit var mAdapter: CommonViewAdapter<Music>
    private var deltaDistance: Int = 0

    override fun getLayoutResID(): Int {
        return R.layout.fragment_daily_recommend
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        DailyRecommendPresenter.instance.subscribe(this)
        deltaDistance = dp2px(context!!, 250f)
    }

    override fun onStart() {
        super.onStart()
        mAdapter = object : CommonViewAdapter<Music>(
            activity!!,
            R.layout.item_local_music,
            mPlayList.getSongs() as ArrayList<Music>
        ) {
            override fun converted(holder: ViewHolder, t: Music, position: Int) {
                val playlist = (list[position])
                holder.setImageResource(R.id.imgCover, playlist.album.picUrl)
                holder.setText(R.id.textViewName, playlist.name)
                holder.setText(R.id.textViewArtist, playlist.artistBeans!![0].name)
            }
        }
        head.title = getString(R.string.daily)
        mPresenter.loadDailyRecommend()
        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(10))
        recyclerView.adapter = mAdapter
        setListener()

    }

    fun setListener() {
        mAdapter.setOnItemClickListener(this)
        head.setOnBackClickListener(this)

        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    cover.alpha = 0f
                }
            }

            override fun onOffsetChanged(appBarLayout: AppBarLayout) {
                val alphaPercent = nest.top.toFloat() / deltaDistance.toFloat()
                Log.d("offset", "$alphaPercent")
                cover.alpha = alphaPercent
            }

        })
    }

    override fun setPresenter(presenter: DailyContract.Presenter) {
        mPresenter = presenter
    }

    override fun onMusicLoad(recommendResult: DailyRecommendResult) {
        Glide.with(activity!!).load(recommendResult.recommend?.get(0)?.album!!.picUrl).into(cover)
        mPlayList.addSong(recommendResult.recommend)
        mAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        currentTime = System.currentTimeMillis()
        mPlayList.setPlayingIndex(position)
        RxBus.getInstance().post(PlayListEvent(mPlayList, position, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:", "DailyRecommendFragment")
    }

    override fun onBackClick(view: View) {
        (activity as MainActivity).onBackPressed()
    }

}
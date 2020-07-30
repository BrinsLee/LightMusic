package com.brins.lightmusic.ui.fragment.dailyrecommend

import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.AppBarStateChangeListener
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.widget.CommonHeaderView
import com.brins.lightmusic.utils.*
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_daily_recommend.*
import kotlinx.android.synthetic.main.fragment_daily_recommend.toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DailyRecommendFragment : BaseFragment(),
    DailyContract.View,
    OnItemClickListener,
    CommonHeaderView.OnBackClickListener {
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    @Inject
    lateinit var mPresenter: DailyRecommendPresenter
    private var mPlayList = PlayList()
    private var currentTime: Long = 0
    private lateinit var mAdapter: CommonViewAdapter<Music>
    private var deltaDistance: Int = 0


    override fun getLayoutResID(): Int {
        return R.layout.fragment_daily_recommend
    }

    override fun onCreateViewAfterBinding() {
        DailyRecommendPresenter.instance.subscribe(this)
        toolbar.setPadding(0, getStatusBarHeight(context!!), 0, 0)
        deltaDistance = dp2px(context!!, 250f)
    }

    override fun onStart() {
        super.onStart()
        mAdapter = object : CommonViewAdapter<Music>(
            activity!!,
            R.layout.item_online_music,
            mPlayList.getSongs() as ArrayList<Music>
        ) {
            override fun converted(holder: ViewHolder, t: Music, position: Int) {
                val playlist = (list[position])
                holder.setImageResource(R.id.item_cover, playlist.album.picUrl)
                holder.setText(R.id.name, playlist.name)
                holder.setText(R.id.artist, playlist.artistBeans!![0].name)
            }
        }
        head.title = getString(R.string.daily)
        loadRecommendData()
        recyclerView.setItemViewCacheSize(5)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SpacesItemDecoration(context!!, 2, R.color.gery))
        recyclerView.adapter = mAdapter
        setListener()

    }

    private fun loadRecommendData() {
        launch({
            showLoading()
            val recommendResult = getDailyMusicData()
            Glide.with(activity!!).load(recommendResult.recommend?.get(0)?.album!!.picUrl)
                .into(cover)
            mPlayList.addSong(recommendResult.recommend)
            mAdapter.notifyDataSetChanged()
        }, {
            Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    override fun showRetryView() {
        super.showRetryView()
        val textError = TextView(context)
        textError.setText(R.string.connect_error)
        textError.textSize = 20f
        textError.setTextColor(ContextCompat.getColor(context!!, R.color.translucent))
        val p: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        p.gravity = Gravity.CENTER
        textError.gravity = Gravity.CENTER
        recommendContainer.addView(textError, p)
        textError.setOnClickListener {
            recommendContainer.removeView(textError)
            loadRecommendData()
        }
    }

    private suspend fun getDailyMusicData() = withContext(Dispatchers.IO) {
        val result = mPresenter.loadDailyRecommend()
        hideLoading()
        result
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


    override fun onItemClick(view: View?, position: Int) {
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
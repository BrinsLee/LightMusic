package com.brins.lightmusic.ui.fragment.artists

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.album.AlbumBean
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.ui.fragment.video.VideoDetailFragment
import com.brins.lightmusic.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_artist_tab.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ArtistTabFragment(var type: Int = 10010, var id: String) :
    BaseFragment(),
    ArtistDetailConstract.View, OnItemClickListener {

    @Inject
    lateinit var mPresenter: ArtistDetailPresenter
    private var playList: PlayList = PlayList()
    private var mvDataBeans: List<LastestMvDataBean> = listOf()
    private var albumDataBeans: MutableList<AlbumBean> = mutableListOf()
    private var currentTime: Long = 0


    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_tab
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mPresenter.subscribe(this)
        initView()
    }

    private fun initView() {
        when (type) {
            SearchType.SONG.type -> loadArtistSong()
            SearchType.MV.type -> loadArtistMv()
            SearchType.ALBUM.type -> loadArtistAlbum()
        }
    }

    private fun loadArtistSong() {
        launch({
            showLoading()
            val result = loadArtistSong(id)
            val mAdapter: CommonViewAdapter<Music>
            playList.addSong(result.hot)
            mAdapter = object : CommonViewAdapter<Music>(
                activity!!, R.layout.item_online_music,
                result.hot!!
            ) {
                override fun converted(holder: ViewHolder, t: Music, position: Int) {
                    val strBuilder = StringBuilder()
                    t.artistBeans?.forEach { strBuilder.append("${it.name} ") }
                    holder.setText(R.id.name, t.name)
                    holder.setText(R.id.artist, strBuilder.toString())
                    holder.getView<ImageView>(R.id.item_cover).visibility = View.GONE
/*                    holder.getView<TextView>(R.id.count).visibility = View.VISIBLE
                    holder.setText(R.id.count, "${position + 1}")*/
                }

            }
            mAdapter.setOnItemClickListener(this)
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(
                SpacesItemDecoration(
                    context!!, 2, R.color.gery
                )
            )
            hideLoading()
        }, {
            Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    private suspend fun loadArtistSong(id: String) = withContext(Dispatchers.IO) {
        val result = mPresenter.loadArtistSong(id)
        result
    }

    private fun loadArtistMv() {
        launch({
            showLoading()
            val result = loadArtistMv(id, 10)
            mvDataBeans = result.dataBeans!!
            val mAdapter: CommonViewAdapter<LastestMvDataBean>
            mAdapter = object : CommonViewAdapter<LastestMvDataBean>(
                activity!!,
                R.layout.item_video_list,
                (mvDataBeans as ArrayList<LastestMvDataBean>)
            ) {
                override fun converted(holder: ViewHolder, t: LastestMvDataBean, position: Int) {
                    holder.setText(R.id.tv_title, t.name)
                    holder.setText(
                        R.id.tv_watch_count,
                        if (t.playCount > 1000) "${t.playCount / 1000}万播放" else "${t.playCount}播放"
                    )
                    holder.setText(R.id.tv_author, t.artistName)
                    holder.setImageResource(R.id.iv_avatar, t.cover)
                    holder.setImageResource(R.id.video_player, t.cover)
                }

            }
            mAdapter.setOnItemClickListener(this)
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            hideLoading()
        }, {
            if (context != null)
                Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    private suspend fun loadArtistMv(id: String, limit: Int) = withContext(Dispatchers.IO) {
        val result = mPresenter.loadArtistMv(id, limit)
        result
    }


    private fun loadArtistAlbum() {
        launch({
            showLoading()
            val response = loadArtistAlbum(id)
            albumDataBeans.addAll(response.hotAlbums!!)
            val mAdapter: CommonViewAdapter<AlbumBean> = object : CommonViewAdapter<AlbumBean>(
                activity!!,
                R.layout.item_online_music,
                albumDataBeans as ArrayList<AlbumBean>
            ) {
                override fun converted(holder: ViewHolder, t: AlbumBean, position: Int) {
                    holder.setImageResource(R.id.item_cover, t.picUrl)
                    holder.setText(R.id.name, t.name)
                    holder.setText(R.id.artist, t.artist!!.name)

                }
            }
            mAdapter.setOnItemClickListener(this)
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(SpacesItemDecoration(context!!, 2, R.color.gery))
            hideLoading()
        }, {
            Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
            showRetryView()
        })
    }

    private suspend fun loadArtistAlbum(id: String) = withContext(Dispatchers.IO) {
        val result = mPresenter.loadArtistAlbum(id)
        result
    }


    override fun onItemClick(view: View?, position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        when (type) {
            SearchType.SONG.type -> {
                currentTime = System.currentTimeMillis()
                playList.setPlayingIndex(position)
                RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
            }
            SearchType.ALBUM.type -> {
                currentTime = System.currentTimeMillis()
                val id = albumDataBeans[position].id
                val bundle = Bundle()
                bundle.putString(TAG, id)
                switch(MusicDetailFragment(), bundle)
            }
            SearchType.MV.type -> {
                currentTime = System.currentTimeMillis()
                launch({
                    var response: Mv? = null
                    withContext(Dispatchers.IO) {
                        response = mPresenter.loadUrl(mvDataBeans[position])
                    }
                    val bundle = Bundle()
                    bundle.putParcelable("Mv", response)
                    switch(VideoDetailFragment(), bundle)
                }, {})
            }
        }
    }


    private fun switch(fragment: Fragment, bundle: Bundle) {
        /* try {
             (activity as MainActivity).switchFragment(fragment, bundle)
                 .addToBackStack(TAG)
                 .commit()
         } catch (e: Exception) {
             e.printStackTrace()
         }*/
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
        container.addView(textError, p)
        textError.setOnClickListener {
            container.removeView(textError)
            initView()
        }
    }
}
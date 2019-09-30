package com.brins.lightmusic.ui.fragment.artists

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.album.AlbumBean
import com.brins.lightmusic.model.album.AlbumListResult
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.ui.fragment.video.VideoDetailFragment
import com.brins.lightmusic.utils.ALBUM
import com.brins.lightmusic.utils.MV
import com.brins.lightmusic.utils.SONG
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import kotlinx.android.synthetic.main.fragment_artist_tab.*

class ArtistTabFragment(var type: Int = 10010, var id: String) :
    BaseFragment<ArtistDetailConstract.Presenter>(),
    ArtistDetailConstract.View, OnItemClickListener {


    private var playList: PlayList = PlayList()
    private var mvDataBeans: List<LastestMvDataBean> = listOf()
    private var albumDataBeans: MutableList<AlbumBean> = mutableListOf()
    private var currentTime: Long = 0
    private var mPresenter: ArtistDetailPresenter? = null


    override fun onArtistSongLoad(result: ArtistSongResult) {
        val mAdapter: CommonViewAdapter<Music>
        playList.addSong(result.hot)
        mAdapter = object : CommonViewAdapter<Music>(
            activity!!, R.layout.item_online_music,
            result.hot!!
        ) {
            override fun converted(holder: ViewHolder, t: Music, position: Int) {
                val strBuilder = StringBuilder()
                t.artistBeans?.forEach { strBuilder.append("${it.name} ") }
                holder.setText(R.id.artist, strBuilder.toString())
                holder.setText(R.id.name, t.name)
                holder.setText(R.id.count, "${position + 1}")
            }

        }
        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!, LinearLayoutManager.VERTICAL
            )
        )

    }


    override fun onArtistMvLoad(result: MvResult) {
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

    }

    override fun onArtistAlbumLoad(response: AlbumListResult) {
        albumDataBeans.addAll(response.hotAlbums!!)
        val mAdapter: CommonViewAdapter<AlbumBean> = object : CommonViewAdapter<AlbumBean>(
            activity!!,
            R.layout.item_local_music,
            albumDataBeans as ArrayList<AlbumBean>
        ) {
            override fun converted(holder: ViewHolder, t: AlbumBean, position: Int) {
                holder.setImageResource(R.id.imgCover, t.picUrl)
                holder.setText(R.id.textViewName, t.name)
                holder.setText(R.id.textViewArtist, t.artist!!.name)

            }
        }
        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }


    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_tab
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        ArtistDetailPresenter.instance.subscribe(this)
        initView()
    }

    private fun initView() {
        when (type) {
            SONG -> mPresenter?.loadArtistSong(id)
            MV -> mPresenter?.loadArtistMv(id, 10)
            ALBUM -> mPresenter?.loadArtistAlbum(id)
        }
    }

    override fun setPresenter(presenter: ArtistDetailConstract.Presenter) {
        mPresenter = presenter as ArtistDetailPresenter

    }

    override fun onArtistMvDetailLoad(response: Mv) {
        val bundle = Bundle()
        bundle.putParcelable("Mv", response)
        switch(VideoDetailFragment(), bundle)
    }


    override fun onItemClick(position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        when (type) {
            SONG -> {
                currentTime = System.currentTimeMillis()
                playList.setPlayingIndex(position)
                RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
            }
            ALBUM -> {
                currentTime = System.currentTimeMillis()
                val id = albumDataBeans[position].id
                val bundle = Bundle()
                bundle.putString(TAG, id)
                switch(MusicDetailFragment(), bundle)
            }
            MV -> {
                currentTime = System.currentTimeMillis()
                mPresenter?.loadUrl(mvDataBeans[position])
            }
        }
    }

    private fun switch(fragment: Fragment, bundle: Bundle) {
        try {
            (activity as MainActivity).switchFragment(fragment, bundle)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
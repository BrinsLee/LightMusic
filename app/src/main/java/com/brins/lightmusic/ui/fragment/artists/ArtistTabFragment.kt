package com.brins.lightmusic.ui.fragment.artists

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.ArtistSongResult
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.musicvideo.LastestMvDataBean
import com.brins.lightmusic.model.musicvideo.MvResult
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.utils.ALBUM
import com.brins.lightmusic.utils.MV
import com.brins.lightmusic.utils.SONG
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import kotlinx.android.synthetic.main.fragment_artist_tab.*

class ArtistTabFragment(var type: Int = 10010, var id: String) :
    BaseFragment<ArtistDetailConstract.Presenter>(),
    ArtistDetailConstract.View {
    private var playList: PlayList = PlayList()
    private var MvDataBeans: List<LastestMvDataBean> = listOf()
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
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (System.currentTimeMillis() - currentTime < 2000) {
                    return
                }
                currentTime = System.currentTimeMillis()
                playList.setPlayingIndex(position)
                RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
            }

        })
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!, LinearLayoutManager.VERTICAL
            )
        )

    }


    override fun onArtistMvLoad(result: MvResult) {
        MvDataBeans = result.dataBeans!!
        val mAdapter: CommonViewAdapter<LastestMvDataBean>
        mAdapter = object : CommonViewAdapter<LastestMvDataBean>(
            activity!!,
            R.layout.item_video_list,
            (result.dataBeans as ArrayList<LastestMvDataBean>)
        ){
            override fun converted(holder: ViewHolder, t: LastestMvDataBean, position: Int) {
                holder.setText(R.id.tv_title,t.name)
                holder.setText(R.id.tv_watch_count, if (t.playCount > 1000) "${t.playCount / 1000}万播放" else "${t.playCount}播放")
                holder.setText(R.id.tv_author, t.artistName)
                holder.setImageResource(R.id.iv_avatar,t.cover)
                holder.setImageResource(R.id.video_player,t.cover)
            }

        }
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    override fun onArtistAlbumLoad() {
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
}
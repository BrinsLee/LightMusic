package com.brins.lightmusic.ui.fragment.search


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.utils.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.fragment.artists.ArtistDetailFragment
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.ui.fragment.video.VideoDetailFragment
import com.brins.lightmusic.utils.SearchType
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_artist_tab.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment(val type: Int = 1) : BaseFragment(),
    SearchContract.View, OnItemClickListener {

    @Inject
    lateinit var mPresenter: SearchPresenter
    private var currentTime: Long = 0
    private var playList: PlayList = PlayList()
    private var albumDataBeans: ArrayList<Album> = arrayListOf()
    private var artistDataBeans: ArrayList<ArtistBean> = arrayListOf()
    private var musicListDataBeans: ArrayList<MusicListBean> = arrayListOf()
    private var musicVideoDataBeans: ArrayList<Mv> = arrayListOf()


    private var layoutId: Int = R.layout.item_local_music
    var keyWords: String = ""
        set(value) {
            field = value
            checkDataLoad()
        }


    private fun checkDataLoad() {
        if (mIsViewBinding && mIsVisibleToUser && keyWords.isNotEmpty()) {
            onLazyLoad()
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        checkDataLoad()
    }


    override fun onLazyLoad() {
        super.onLazyLoad()
        if (mIsViewBinding && keyWords.isNotEmpty()) {
            loadSearchData()
        }
    }

    private fun loadSearchData() {
        when (type) {
            SearchType.MUSIC.type -> {
                layoutId = R.layout.item_online_music
                searchMusic()
            }

            SearchType.ALBUMS.type -> {
                searchAlbum()
            }

            SearchType.ARTIST.type -> {
                searchArtist()
            }

            SearchType.MUSICLIST.type -> {
                searchMusicList()
            }
            SearchType.MUSICVIDEO.type -> {
                searchMusicVideo()
            }
        }
    }

    private fun searchMusicVideo() {
        launch({
            showLoading()
            val mSearchResult = searchMusicVideoData()
            if (musicVideoDataBeans.isNotEmpty()) {
                musicVideoDataBeans.clear()
            }
            musicVideoDataBeans.addAll(mSearchResult)
            initRecyclerView(musicVideoDataBeans)
        }, {
            hideLoading()
            showRetryView()
        })
    }


    private fun searchMusicList() {
        launch({
            showLoading()
            val mSearchResult = searchMusicListData()
            if (musicListDataBeans.isNotEmpty()) {
                musicListDataBeans.clear()
            }
            musicListDataBeans.addAll(mSearchResult.dataBean?.data!!)
            initRecyclerView(musicListDataBeans)
        }, {
            hideLoading()
            showRetryView()
        })
    }


    private fun searchArtist() {
        launch({
            showLoading()
            val mSearchResult = searchArtistData()
            if (artistDataBeans.isNotEmpty()) {
                artistDataBeans.clear()
            }
            artistDataBeans.addAll(mSearchResult.dataBean?.data!!)
            initRecyclerView(artistDataBeans)
        }, {
            Log.d("error", it.message)
            hideLoading()
            showRetryView()
        })
    }


    private fun searchAlbum() {
        launch({
            showLoading()
            val mSearchResult = searchAlbumData()
            if (albumDataBeans.isNotEmpty()) {
                albumDataBeans.clear()
            }
            albumDataBeans.addAll(mSearchResult.dataBean?.data!!)
            initRecyclerView(albumDataBeans)
        }, {
            hideLoading()
            showRetryView()
        })
    }


    private fun searchMusic() {
        launch({
            showLoading()
            val mSearchResult = searchMusicData()
            playList.addSong(mSearchResult.dataBean?.data)
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    context!!, LinearLayoutManager.VERTICAL
                )
            )
            initRecyclerView(mSearchResult.dataBean?.data)
        }, {
            hideLoading()
            showRetryView()
        })
    }

    private fun <T> initRecyclerView(data: ArrayList<T>?) {
        if (data == null && recyclerView == null) {
            return
        }
        val mAdapter = object : CommonViewAdapter<T>(
            activity!!
            , layoutId, data!!
        ) {
            override fun converted(holder: ViewHolder, t: T, position: Int) {
                if (t is Music) {
                    val strBuilder = StringBuilder()
                    t.artistBeans?.forEach { strBuilder.append("${it.name} ") }
                    holder.setText(R.id.artist, strBuilder.toString())
                    holder.setText(R.id.name, t.name)
                    holder.setText(R.id.count, "${position + 1}")
                }
                if (t is Album) {
                    holder.setImageResource(R.id.imgCover, t.picUrl)
                    holder.setText(R.id.textViewName, t.name)
                    holder.setText(R.id.textViewArtist, t.artist!!.name)
                }

                if (t is ArtistBean) {
                    holder.setImageResource(R.id.imgCover, t.picUrl)
                    holder.setText(R.id.textViewName, t.name)
                    holder.setText(R.id.textViewArtist, "")
                }
                if (t is MusicListBean) {
                    holder.setImageResource(R.id.imgCover, t.coverImgUrl)
                    holder.setText(R.id.textViewName, t.name)
                    holder.setText(
                        R.id.textViewArtist,
                        if (t.playCount > 10000) "${t.playCount / 10000}万播放" else {
                            "${t.playCount}播放"
                        }
                    )
                }
                if (t is Mv) {
                    holder.setImageResource(R.id.imgCover, t.dataBean.cover)
                    holder.setText(R.id.textViewName, t.dataBean.name)
                    holder.setText(R.id.textViewArtist, t.dataBean.artistName)
                }

            }
        }
        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        hideLoading()
    }

    private suspend fun searchMusicData() = withContext(Dispatchers.IO) {
        val result = mPresenter.searchMusicData(keyWords, type)
        result
    }

    private suspend fun searchAlbumData() = withContext(Dispatchers.IO) {
        val result = mPresenter.searchAlbumData(keyWords, type)
        result
    }

    private suspend fun searchArtistData() = withContext(Dispatchers.IO) {
        val result = mPresenter.searchArtistData(keyWords)
        result
    }

    private suspend fun searchMusicListData() = withContext(Dispatchers.IO) {
        val result = mPresenter.searchMusicListData(keyWords)
        result
    }

    private suspend fun searchMusicVideoData() = withContext(Dispatchers.IO) {
        val result = mPresenter.searchMusicVideoData(keyWords)
        result
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
            loadSearchData()
        }
    }


    override fun onCreateViewAfterBinding() {
        super.onCreateViewAfterBinding()
        SearchPresenter.instance.subscribe(this)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_tab
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }

    private fun finish() {
        (activity as MainActivity).onBackPressed()
    }

    override fun onItemClick(view: View?,position: Int) {
        if (System.currentTimeMillis() - currentTime < 2000) {
            return
        }
        when (type) {
            SearchType.MUSIC.type -> {
                currentTime = System.currentTimeMillis()
                playList.setPlayingIndex(position)
                RxBus.getInstance().post(PlayListEvent(playList, position, TYPE_ONLINE_MUSIC))
            }
            SearchType.ALBUMS.type -> {
                currentTime = System.currentTimeMillis()
                val id = albumDataBeans[position].id
                val bundle = Bundle()
                bundle.putString("ArtistTabFragment", id)
                switch(MusicDetailFragment(), bundle)
            }
            SearchType.ARTIST.type -> {
                currentTime = System.currentTimeMillis()
                val bundle = Bundle()
                bundle.putParcelable("ARTIST", artistDataBeans[position])
                switch(ArtistDetailFragment(), bundle)

            }
            SearchType.MUSICLIST.type -> {
                currentTime = System.currentTimeMillis()
                val bundle = Bundle()
                bundle.putString("DiscoveryFragment", musicListDataBeans[position].id)
                switch(MusicDetailFragment(), bundle)
            }
            SearchType.MUSICVIDEO.type -> {
                currentTime = System.currentTimeMillis()
                val bundle = Bundle()
                bundle.putParcelable("Mv", musicVideoDataBeans[position])
                switch(VideoDetailFragment(), bundle)
            }
        }
    }

    private fun switch(fragment: Fragment, bundle: Bundle) {
/*        try {
            (activity as SearchActivity).switchFragment(fragment, bundle)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

}

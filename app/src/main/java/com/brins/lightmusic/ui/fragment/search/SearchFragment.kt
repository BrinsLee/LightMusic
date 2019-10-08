package com.brins.lightmusic.ui.fragment.search


import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.RxBus
import com.brins.lightmusic.event.PlayListEvent
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.artist.Album
import com.brins.lightmusic.model.loaclmusic.PlayList
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.activity.SearchActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.fragment.discovery.MusicDetailFragment
import com.brins.lightmusic.utils.SearchType
import com.brins.lightmusic.utils.TYPE_ONLINE_MUSIC
import com.brins.lightmusic.utils.launch
import kotlinx.android.synthetic.main.fragment_artist_tab.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SearchFragment(val type: Int = 1) : BaseFragment<SearchContract.Presenter>(),
    SearchContract.View, OnItemClickListener {

    private var currentTime: Long = 0
    private var mPresenter: SearchPresenter? = null
    private var playList: PlayList = PlayList()
    private var albumDataBeans: ArrayList<Album> = arrayListOf()
    private var layoutId: Int = 0
    var keyWords: String = ""
        set(value) {
            field = value
            checkLoad()
        }


    private fun checkLoad() {
        if (mIsViewBinding && mIsVisibleToUser && keyWords.isNotEmpty()) {
            onLazyLoad()
        }
    }


    override fun onLazyLoad() {
        super.onLazyLoad()
        if (keyWords.isNotEmpty()) {
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
                layoutId = R.layout.item_local_music
                searchAlbum()
            }
        }
    }

    private fun searchAlbum() {
        launch({
            showLoading()
            val mSearchResult = searchAlbumData()
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
        if (data == null) {
            return
        }
        val mAdapter = object : CommonViewAdapter<T>(
            activity!!
            , layoutId, data
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

            }
        }
        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        hideLoading()
    }

    private suspend fun searchMusicData() = withContext(Dispatchers.IO) {
        val result = mPresenter!!.searchMusicData(keyWords, type)
        result
    }

    private suspend fun searchAlbumData() = withContext(Dispatchers.IO) {
        val result = mPresenter!!.searchAlbumData(keyWords, type)
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

    override fun setPresenter(presenter: SearchContract.Presenter) {
        mPresenter = presenter as SearchPresenter
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        SearchPresenter.instance.subscribe(this)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_tab
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unsubscribe()
    }

    private fun finish() {
        (activity as MainActivity).onBackPressed()
    }

    override fun onItemClick(position: Int) {
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
        }
    }

    private fun switch(fragment: Fragment, bundle: Bundle) {
        try {
            (activity as SearchActivity).switchFragment(fragment, bundle)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

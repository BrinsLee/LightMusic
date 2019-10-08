package com.brins.lightmusic.ui.activity


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import com.brins.lightmusic.model.search.SearchSuggestResult
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.search.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.appcompat.R
import com.brins.lightmusic.ui.adapter.VideoPagerAdapter
import com.brins.lightmusic.ui.fragment.search.SearchFragment
import com.brins.lightmusic.utils.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.mTablayout
import kotlinx.android.synthetic.main.activity_search.mViewpager


class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {


    private var mImm: InputMethodManager? = null
    private var queryString = ""
    private lateinit var searchSuggest: SearchSuggestResult
    private lateinit var names: Array<String?>
    val adapter by lazy { VideoPagerAdapter(supportFragmentManager, list, mTitle!!) }
    private var list = mutableListOf<SearchFragment>()
    private var mTitle: Array<String>? = null
    private val musicFragment = SearchFragment(SearchType.MUSIC.type)
    private val albumFragment = SearchFragment(SearchType.ALBUMS.type)
    private val artistFragment = SearchFragment(SearchType.ARTIST.type)
    private val musicListFragment = SearchFragment(SearchType.MUSICLIST.type)
    private val mvFragment = SearchFragment(SearchType.MUSICVIDEO.type)

    companion object {
        fun startThis(activity: Activity) {
            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreateAfterBinding(savedInstanceState: Bundle?) {
        super.onCreateAfterBinding(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initData()
        initView()
    }

    private fun initView() {
        mViewpager.adapter = adapter
        mTablayout.setupWithViewPager(mViewpager)
        for (i in 0 until adapter.count) {
            mTablayout.getTabAt(i)!!.customView = adapter.getTabView(this, i)
            mTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(com.brins.lightmusic.R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.GRAY)
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(com.brins.lightmusic.R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.BLACK)
                }

            })
        }
        mSearchView.setOnQueryTextListener(this)
        mSearchView.queryHint = getString(com.brins.lightmusic.R.string.search_hint)
        mSearchView.setIconifiedByDefault(false)
        mSearchView.requestFocus()
        val searchIcon =
            mSearchView.findViewById(R.id.search_mag_icon) as ImageView
        mSearchView.post {
            searchIcon.setImageDrawable(null)
            searchIcon.visibility = View.GONE
        }
//        menu.findItem(R.id.menuSearch).expandActionView()
        mSearchView.setOnSearchClickListener {
            list_item.visibility = View.VISIBLE
        }
        mSearchView.setOnCloseListener {
            list_item.visibility = View.GONE
            false
        }
        list_item.onItemClickListener = this

    }

    override fun getLayoutResId(): Int {
        return com.brins.lightmusic.R.layout.activity_search
    }


    private fun initData() {
        mTitle = arrayOf("单曲", "专辑", "歌手", "歌单", "MV")
        list.add(musicFragment)
        list.add(albumFragment)
        list.add(artistFragment)
        list.add(musicListFragment)
        list.add(mvFragment)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        hideInputManager()
        list_item.visibility = View.GONE
        for (fragment : SearchFragment in list){
            fragment.keyWords = queryString
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText == queryString) {
            return true
        }
        queryString = newText
        if (queryString.trim { it <= ' ' } != "") {
            launch({
                searchSuggest = getSearchSuggest(queryString)
                if (searchSuggest.result != null) {
                    names = arrayOfNulls<String>(searchSuggest.result!!.allMatch!!.size)
                    for ((i, a) in searchSuggest.result!!.allMatch!!.withIndex()) {
                        names[i] = a.keyword
                    }
                    list_item.visibility = View.VISIBLE
                    list_item.adapter = ArrayAdapter<String>(
                        this
                        , android.R.layout.simple_list_item_1, names
                    )
                }

            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
        }
        return true
    }


    private suspend fun getSearchSuggest(keys: String) = withContext(Dispatchers.IO) {
        val result = SearchPresenter.instance.loadSearchSuggest(keys)
        result
    }


    private fun hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm!!.hideSoftInputFromWindow(mSearchView.windowToken, 0)
            }
            mSearchView.clearFocus()

//            SearchHistory.getInstance(this).addSearchString(mSearchView.query.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (!::names.isInitialized) {
            return
        }
        if (position > names.size) {
            return
        }
        queryString = names[position]!!
        mSearchView.setQuery(queryString, true)
    }
}

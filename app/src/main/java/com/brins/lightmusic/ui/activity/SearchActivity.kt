package com.brins.lightmusic.ui.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.brins.lightmusic.model.search.SearchSuggestResult
import com.brins.lightmusic.ui.base.BaseActivity
import com.brins.lightmusic.ui.fragment.search.SearchPresenter
import com.brins.lightmusic.utils.SEARCH_KEY
import com.brins.lightmusic.utils.launch
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.appcompat.R


class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {


    private var mImm: InputMethodManager? = null
    private var queryString = ""
    private lateinit var searchSuggest: SearchSuggestResult
    private lateinit var names: Array<String?>


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

        mSearchView.setOnQueryTextListener(this)
        mSearchView.queryHint = getString(com.brins.lightmusic.R.string.search_hint)
        mSearchView.setIconifiedByDefault(false)
        mSearchView.requestFocus()
        val searchIcon =
            mSearchView.findViewById(R.id.search_mag_icon) as ImageView
        mSearchView.post {
            searchIcon.setImageDrawable(null)
            searchIcon.setVisibility(View.GONE)
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


    override fun onStart() {
        super.onStart()
//        showBottomBar(supportFragmentManager)
    }

    override fun onDestroy() {
        super.onDestroy()
//        removeBottomBar(supportFragmentManager)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        hideInputManager()
        val bundle = Bundle()
        bundle.putString(SEARCH_KEY, query)
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
        if(position > names.size){
            return
        }
        mSearchView.setQuery(names[position], false)
    }
}

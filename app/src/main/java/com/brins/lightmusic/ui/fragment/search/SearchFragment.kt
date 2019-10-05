package com.brins.lightmusic.ui.fragment.search


import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat

import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.ArrayList
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class SearchFragment : BaseFragment<SearchContract.Presenter>(), SearchView.OnQueryTextListener {



    private var mPresenter: SearchPresenter? = null
    private var mImm: InputMethodManager? = null
    private var queryString: String = ""
    private var searchResults: ArrayList<Music> = arrayListOf()


    override fun setPresenter(presenter: SearchContract.Presenter) {
        mPresenter = presenter as SearchPresenter
    }

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        mSearchView.setOnQueryTextListener(this)


    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_search
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun finish() {
        (activity as MainActivity).onBackPressed()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return false

    }

    override fun onQueryTextChange(newText: String): Boolean {
       return false
    }

    fun hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm!!.hideSoftInputFromWindow(mSearchView.windowToken, 0)
            }
            mSearchView.clearFocus()

//            SearchHistory.getInstance(this).addSearchString(mSearchView.query.toString())
        }
    }




}

package com.brins.lightmusic.ui.fragment.artists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.ViewHolder
import com.brins.lightmusic.ui.customview.CommonHeaderView
import com.brins.lightmusic.utils.launch
import kotlinx.android.synthetic.main.fragment_artist_category.recyclerView
import kotlinx.android.synthetic.main.fragment_localmusic.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistCatgoryFragment : BaseFragment<ArtistConstract.Presenter>(), ArtistConstract.View,
    OnItemClickListener, CommonHeaderView.OnBackClickListener {


    private lateinit var mPresenter: ArtistPresenter
    private var type: Int = 0
    private var mList = arrayListOf<ArtistBean>()

    override fun onCreateViewAfterBinding(view: View) {
        super.onCreateViewAfterBinding(view)
        ArtistPresenter.instance.subscribe(this)
        if (arguments != null) {
            type = arguments?.getInt("category", 0)!!
        }
        ArtistPresenter.instance.subscribe(this)
        toolbar.setOnBackClickListener(this)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_category
    }

    override fun setPresenter(presenter: ArtistConstract.Presenter) {
        mPresenter = presenter as ArtistPresenter
        if (type != 0) {
            getCategory()
        }
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("ARTIST", mList[position])
        switch(ArtistDetailFragment(), bundle)
    }

    override fun onBackClick(view: View) {
        activity!!.onBackPressed()

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

    private fun getCategory() {
        launch({
            showLoading()
            val mResult = getCategoryData()
            mList.addAll(mResult.artists!!)
            initRecyclerView(mList)
        }, {
            hideLoading()
            showRetryView()
        })
    }

    private fun initRecyclerView(data: ArrayList<ArtistBean>) {
        if (data == null && recyclerView == null) {
            return
        } else {
            val mAdapter = object :
                CommonViewAdapter<ArtistBean>(activity!!, R.layout.item_local_music, data) {
                override fun converted(holder: ViewHolder, t: ArtistBean, position: Int) {
                    holder.setImageResource(R.id.imgCover, t.picUrl)
                    holder.setText(R.id.textViewName, t.name)
                    holder.setText(
                        R.id.textViewArtist,
                        "${t.musicSize} ${context.getString(R.string.num_songs)}"
                    )
                }

            }
            mAdapter.setOnItemClickListener(this)
            recyclerView.adapter = mAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
            hideLoading()
        }
    }

    private suspend fun getCategoryData() = withContext(Dispatchers.IO) {
        val result = mPresenter.loadArtistCategory(type)
        result
    }


}
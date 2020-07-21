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
import javax.inject.Inject

class ArtistCatgoryFragment : BaseFragment(), ArtistConstract.View,
    OnItemClickListener, CommonHeaderView.OnBackClickListener {
    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    @Inject
    lateinit var mPresenter: ArtistPresenter
    private var type: Int = 0
    private var mList = arrayListOf<ArtistBean>()

    override fun onCreateViewAfterBinding() {
        super.onCreateViewAfterBinding()
        ArtistPresenter.instance.subscribe(this)
        if (arguments != null) {
            type = arguments?.getInt("category", 0)!!
        }
        toolbar.setOnBackClickListener(this)
        if (type != 0) {
            getCategory()
        }

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_category
    }



    override fun onItemClick(view: View?, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("ARTIST", mList[position])
        switch(ArtistDetailFragment(), bundle)
    }

    override fun onBackClick(view: View) {
        activity!!.onBackPressed()

    }

    private fun switch(fragment: Fragment, bundle: Bundle) {
        /*try {
            (activity as MainActivity).switchFragment(fragment, bundle)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
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
        if (recyclerView == null) {
            return
        } else {
            val mAdapter = object :
                CommonViewAdapter<ArtistBean>(activity!!, R.layout.item_online_music, data) {
                override fun converted(holder: ViewHolder, t: ArtistBean, position: Int) {
                    holder.setImageResource(R.id.item_cover, t.picUrl)
                    holder.setText(R.id.name, t.name)
                    holder.setText(
                        R.id.artist,
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
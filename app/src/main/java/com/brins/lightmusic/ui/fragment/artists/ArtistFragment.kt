package com.brins.lightmusic.ui.fragment.artists


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.model.artist.Category
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.customview.PileLayout
import com.brins.lightmusic.utils.launch
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_artist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArtistFragment : BaseFragment<ArtistConstract.Presenter>(), ArtistConstract.View,
    OnItemClickListener {


    private lateinit var artistCategory: ArrayList<Category>
    private lateinit var artistList: ArrayList<ArtistBean>
    private lateinit var mPresenter: ArtistPresenter
    private val mAdapter: ArtistAdapter by lazy { ArtistAdapter(artistList) }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist
    }


    override fun onLazyLoad() {
        ArtistPresenter.instance.subscribe(this@ArtistFragment)
        launch({
            showLoading()
            val artistCategory = getCategoryData()
            onArtistCategoryLoad(artistCategory)
            val artistData = getArtistData()
            onArtistLoad(artistData)

        },{
            Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show()
            hideLoading()
        })
    }

    private suspend fun getCategoryData() = withContext(Dispatchers.IO){
        val result = mPresenter.loadArtistCategory()
        result
    }

    private suspend fun getArtistData() = withContext(Dispatchers.IO){
        val result = mPresenter.loadArtist()
        result
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("ARTIST", artistList[position])
        switch(ArtistDetailFragment(), bundle)
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

    //MVP View
    private fun onArtistLoad(artistList: ArrayList<ArtistBean>) {
        this.artistList = artistList
        initRecyclerView()
        hideLoading()
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)
        recyclerArtist.layoutManager = LinearLayoutManager(context)
        recyclerArtist.adapter = mAdapter
    }

    private fun onArtistCategoryLoad(category: CategoryResult) {
        if (category.artists != null && category.artists!!.isNotEmpty()) {
            artistCategory = category.artists!!
            initPileLayout()
        }

    }

    private fun initPileLayout() {
        pileLayout.visibility = View.VISIBLE
        pileLayout.setAdapter(object : PileLayout.Adapter() {
            override fun getLayoutId(): Int {
                return R.layout.artist_item
            }

            override fun getItemCount(): Int {
                return artistCategory.size
            }

            override fun bindView(view: View, index: Int) {
                var viewHolder: ViewHolder? = view.tag as ViewHolder?
                if (viewHolder == null) {
                    viewHolder = ViewHolder()
                    viewHolder.imageView = view.findViewById(R.id.iv_recovery)
                    viewHolder.textView = view.findViewById(R.id.introduce)
                    view.tag = viewHolder
                }
                viewHolder.textView!!.text = artistCategory[index].name
                Glide.with(this@ArtistFragment)
                    .load(artistCategory[index].url)
                    .into(viewHolder.imageView!!)
            }
        })
    }

    override fun setPresenter(presenter: ArtistConstract.Presenter) {
        mPresenter = presenter as ArtistPresenter
    }

    class ViewHolder {
        var imageView: ImageView? = null
        var textView: TextView? = null
    }
}

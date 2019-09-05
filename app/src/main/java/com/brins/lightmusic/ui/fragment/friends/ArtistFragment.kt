package com.brins.lightmusic.ui.fragment.friends


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.model.artist.Category
import com.brins.lightmusic.model.artist.CategoryResult
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.customview.PileLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_artist.*

class ArtistFragment : BaseFragment(), ArtistConstract.View {


    private var artistCategory: ArrayList<Category> = arrayListOf()
    private lateinit var mPresenter: ArtistPresenter

    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist
    }


    override fun onLazyLoad() {
        super.onLazyLoad()
        ArtistPresenter.instance.subscribe(this@ArtistFragment)

    }

    //MVP View
    override fun onArtistLoad() {
    }

    override fun onArtistCategoryLoad(category: CategoryResult) {

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

    override fun handleError(error: Throwable) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun setPresenter(presenter: ArtistConstract.Presenter) {
        mPresenter = presenter as ArtistPresenter
        mPresenter.loadArtistCategory()
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    class ViewHolder {
        var imageView: ImageView? = null
        var textView: TextView? = null
    }
}

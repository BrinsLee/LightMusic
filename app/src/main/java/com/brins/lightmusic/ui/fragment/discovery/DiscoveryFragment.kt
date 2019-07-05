package com.brins.lightmusic.ui.fragment.discovery


import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Artist
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.MusicList
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.customview.PileLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_discovery.*

class DiscoveryFragment : BaseFragment() , DiscoveryContract.View{

    lateinit var mPresenter: DiscoveryContract.Presenter
    lateinit var artistlist : MutableList<Artist>


    override fun onLazyLoad() {
        super.onLazyLoad()
        getArtist()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getArtist() {
        DiscoverPresent(this@DiscoveryFragment).subscribe()
    }

    //MVP View
    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getLifeActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }

    override fun getcontext(): Context {
        return context!!
    }



    override fun handleError(error: Throwable) {

    }

    override fun onMusicListLoad(songs: MutableList<MusicList>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onArtistLoad(artists: MutableList<Artist>) {
        artistlist = artists
        initArtistView()
    }

    private fun initArtistView() {
        pileLayout.visibility = View.VISIBLE
        pileLayout.setAdapter(object : PileLayout.Adapter(){
            override fun getLayoutId(): Int {
                return R.layout.artist_item
            }

            override fun getItemCount(): Int {
                return artistlist.size
            }

            override fun bindView(view: View, index: Int) {
                var viewHolder : ViewHolder? = view.tag as ViewHolder
                if (viewHolder == null){
                    viewHolder = ViewHolder()
                    viewHolder.imageView = view.findViewById(R.id.iv_recovery)
                    viewHolder.textView = view.findViewById(R.id.introduce)
                    view.tag = viewHolder
                }
                viewHolder.textView!!.text = artistlist[index].name
                Glide.with(this@DiscoveryFragment)
                    .load(artistlist[index].picUrl)
                    .into(viewHolder.imageView!!)
            }
        })

    }

    override fun setPresenter(presenter: DiscoveryContract.Presenter?) {
        mPresenter = presenter!!
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_discovery
    }

    class ViewHolder {
        var imageView: ImageView? = null
        var textView : TextView? = null
    }

}

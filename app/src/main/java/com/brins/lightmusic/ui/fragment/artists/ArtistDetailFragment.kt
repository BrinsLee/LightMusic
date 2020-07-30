package com.brins.lightmusic.ui.fragment.artists

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.brins.lightmusic.R
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.activity.SearchActivity
import com.brins.lightmusic.ui.adapter.VideoPagerAdapter
import com.brins.lightmusic.ui.base.AppBarStateChangeListener
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.widget.CommonHeaderView
import com.brins.lightmusic.utils.*
import com.brins.lightmusic.utils.GlideHelper.GlideHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_artist_detail.*
import kotlinx.android.synthetic.main.fragment_artist_detail.appBar
import kotlinx.android.synthetic.main.fragment_artist_detail.cover
import kotlinx.android.synthetic.main.fragment_artist_detail.head
import kotlinx.android.synthetic.main.fragment_artist_detail.mTablayout
import kotlinx.android.synthetic.main.fragment_artist_detail.mViewpager


class ArtistDetailFragment : BaseFragment(),
    CommonHeaderView.OnBackClickListener {
    override fun initInject() {
        getFragmentComponent().inject(this)
    }


    private var deltaDistance: Int = 0
    private var mArtist: ArtistBean? = null
    private var mTitle: Array<String>? = null


    private var list = mutableListOf<Fragment>()
    val adapter by lazy { VideoPagerAdapter(childFragmentManager, list, mTitle!!) }


    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_detail
    }


    override fun onCreateViewAfterBinding() {
        super.onCreateViewAfterBinding()
        setListener()
        mArtist = arguments?.getParcelable("ARTIST")
        if (mArtist != null) {
            GlideHelper.setRoundImageResource(avatar, mArtist!!.picUrl, 10)
            GlideHelper.setBlurImageResource(cover,mArtist!!.picUrl)
            /*ImageLoadreUtils.getInstance().loadImage(
                context,
                ImageLoader.Builder().url(mArtist!!.picUrl).assignWidth(500).assignHeight(500)
                    .bitmapTransformation(
                        CornersTransform(20f)
                    ).bulid(), object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val result = rsBlur(context!!, resource, 25f)
                        cover.setImageBitmap(result)
                    }
                }
            )*/
            name.text = mArtist!!.name
            initData()
            initView()
        }
    }

    private fun setListener() {
        toolbar.setNavigationOnClickListener {
            try {
                (activity as MainActivity).onBackPressed()

            } catch (e: Exception) {
                (activity as SearchActivity).onBackPressed()

            }
        }
        head.setOnBackClickListener(this)
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    bg_mask.alpha = 0f
                    cover.alpha = 0f
                }
            }

            override fun onOffsetChanged(appBarLayout: AppBarLayout) {
                val alphaPercent = nest.top.toFloat() / deltaDistance.toFloat()
                Log.d("offset", "$alphaPercent")
                bg_mask.alpha = alphaPercent
                cover.alpha = alphaPercent
            }

        })
    }

    private fun initData() {
        mTitle = arrayOf("单曲(${mArtist!!.musicSize})", "专辑(${mArtist!!.albumSize})", "MV")
        deltaDistance = dp2px(context!!, 250f)
        list.add(ArtistTabFragment(SearchType.SONG.type, mArtist!!.id))
        list.add(ArtistTabFragment(SearchType.ALBUM.type, mArtist!!.id))
        list.add(ArtistTabFragment(SearchType.MV.type, mArtist!!.id))
    }

    private fun initView() {
        toolbar.title = mArtist!!.name
        toolbar.setNavigationIcon(R.drawable.ic_back_black)
        mViewpager.adapter = adapter
        mTablayout.setupWithViewPager(mViewpager)
        for (i in 0 until adapter.count) {
            mTablayout.getTabAt(i)!!.customView = adapter.getTabView(activity!!, i)
            mTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.GRAY)
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    val tv_tab = p0!!.customView!!.findViewById(R.id.tab_item) as TextView
                    tv_tab.setTextColor(Color.BLACK)
                }

            })
        }
    }

    override fun onBackClick(view: View) {


    }
}
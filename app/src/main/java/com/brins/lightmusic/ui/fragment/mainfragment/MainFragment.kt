package com.brins.lightmusic.ui.fragment.mainfragment


import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.activity.MainActivity
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.brins.lightmusic.ui.base.adapter.CommonViewAdapter
import com.brins.lightmusic.ui.customview.CustomStaggeredGridLayoutManager
import com.brins.lightmusic.ui.fragment.discovery.DiscoverPresenter
import com.brins.lightmusic.ui.fragment.discovery.DiscoveryContract
import com.brins.lightmusic.utils.launch
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.animation.BaseAnimation
import kotlinx.android.synthetic.main.fragment_my.*
import javax.inject.Inject

class MainFragment : BaseFragment(), MainContract.View, OnItemClickListener,
    View.OnClickListener {

    override fun initInject() {
        getFragmentComponent().inject(this)
    }

    private lateinit var mAdapter: PersonalizedMusicAdapter

    /*private lateinit var customStaggeredGridLayoutManager: CustomStaggeredGridLayoutManager


    private lateinit var mAdapter: CommonViewAdapter<UserPlayListBean>
    private var isListOpen = false*/
    @Inject
    lateinit var mPresenter: MainPresenter

    override fun getLayoutResID(): Int {
        return R.layout.fragment_my
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mPresenter.subscribe(this)
        launch({
            mPresenter.loadPersonalizedMusic()
        }, {})
        setListener()
    }


    private fun setListener() {

    }


    override fun onItemClick(view: View?, position: Int) {
        view?.let {
            /*UserPlayListActivity.startThis(
                view.findViewById(R.id.imgCover) as ImageView,
                mPlayList[position],
                activity as Activity
            )*/
        }
        /*try {
            (activity as MainActivity).switchFragment(UserMusicListFragment(mPlayList[position]))
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }*/
    }

    override fun onClick(v: View) {
        /*when (v.id) {
            R.id.avatar, R.id.nickName -> LoginActivity.startThisActivity(this, AppConfig.isLogin)
            R.id.fm -> mPresenter.loadUserFm()
            R.id.recommend -> switch(DailyRecommendFragment())
            R.id.localMusic -> switch(LocalMusicFragment())
            R.id.rootli -> openMusicList()
        }*/

    }

    /*private fun openMusicList() {
        isListOpen = !isListOpen
        if (isListOpen) {
            userPlayList.visibility = View.VISIBLE
            status.setImageResource(R.drawable.ic_chevron_down)
        } else {
            userPlayList.visibility = View.INVISIBLE
            status.setImageResource(R.drawable.ic_chevron_right)
        }
    }*/

    private fun switch(fragment: Fragment) {
        try {
            (activity as MainActivity).switchFragment(fragment)
                .addToBackStack(TAG)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPersonalizedMusicLoad(data: ArrayList<PersonalizedMusic>?) {
        data?.let {
            mAdapter = PersonalizedMusicAdapter(it)
            mAdapter.animationEnable = true
            mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInLeft)
            mAdapter.isAnimationFirstOnly = false
            recommendList.adapter = mAdapter
            recommendList.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
    }

    //MVP View

    /*override fun onFmLoad(result: UserFmResult) {
        mFmList = PlayList()
        mFmList.addSong(result.fmList)
        mFmList.setPlayingIndex(0)
        RxBus.getInstance().post(PlayListEvent(mFmList, 0, TYPE_ONLINE_MUSIC))
        Log.d("RxBus:", "UserMusicListActivity")
        hideLoading()
    }

    override fun onUserProfileLoad() {
    }

    override fun onUserMusicListLoad(result: UserPlayListResult) {
        if (result.playlist != null) {
            mPlayList = result.playlist!!
            mAdapter = object :
                CommonViewAdapter<UserPlayListBean>(
                    activity!!,
                    R.layout.item_local_music,
                    mPlayList
                ) {
                override fun converted(holder: ViewHolder, t: UserPlayListBean, position: Int) {
                    val playlist = list[position]
                    val width = (ScreenUtils.getScreenWidth(context) - dp2px(context, 25f)) / 2
                    val layoutparams = holder.getView<ImageView>(R.id.imgCover).layoutParams
                    layoutparams.width = width
                    layoutparams.height = width
                    holder.getView<ImageView>(R.id.imgCover).layoutParams = layoutparams
                    ImageLoadreUtils.getInstance().loadImage(
                        context,
                        ImageLoader.Builder().url(playlist.coverImgUrl).assignWidth(width)
                            .assignHeight(
                                width
                            )
                            .scaleModeType(ImageLoadreUtils.SCALE_MODE_CENTER_CROP).imgView(
                                holder.getView(
                                    R.id.imgCover
                                )
                            ).bulid()
                    )
                    holder.setText(R.id.textViewName, playlist.name)
                    holder.setText(R.id.textViewArtist, "共${playlist.trackCount}首")
                }
            }
            customStaggeredGridLayoutManager =
                CustomStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            customStaggeredGridLayoutManager.setScrollEnabled(false)
            userPlayList.isNestedScrollingEnabled = false
            userPlayList.layoutManager = customStaggeredGridLayoutManager
            userPlayList.adapter = mAdapter
            mAdapter.setOnItemClickListener(this)
        }
    }


    override fun onLoadFail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun checkToLoad() {
        if (mPlayList.isEmpty()) {
            mPresenter.loadUserMusicList(AppConfig.userAccount.id)
        }
    }*/

}

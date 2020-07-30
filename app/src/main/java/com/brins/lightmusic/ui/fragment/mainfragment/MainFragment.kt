package com.brins.lightmusic.ui.fragment.mainfragment


import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.lightmusic.R
import com.brins.lightmusic.model.personal.PersonalizedMusic
import com.brins.lightmusic.ui.base.BaseFragment
import com.brins.lightmusic.utils.launch
import com.chad.library.adapter.base.BaseQuickAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(), MainContract.View {


    private lateinit var mAdapter: PersonalizedMusicAdapter

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
    }

    override fun onPersonalizedMusicLoad(data: ArrayList<PersonalizedMusic>?) {
        data?.let {
            mAdapter = PersonalizedMusicAdapter(activity!!, it)
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

}

package com.brins.lightmusic.ui.fragment.artists

import com.brins.lightmusic.R
import com.brins.lightmusic.ui.base.BaseFragment

class ArtistTabFragment : BaseFragment<ArtistDetailConstract.Presenter>() {
    override fun getLayoutResID(): Int {
        return R.layout.fragment_artist_tab
    }

    override fun setPresenter(presenter: ArtistDetailConstract.Presenter) {
    }
}
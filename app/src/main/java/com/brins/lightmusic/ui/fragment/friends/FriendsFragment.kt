package com.brins.lightmusic.ui.fragment.friends


import com.brins.lightmusic.R
import com.brins.lightmusic.ui.base.BaseFragment

class FriendsFragment : BaseFragment() {
    override fun getLayoutResID(): Int {
        return R.layout.fragment_friends
    }

    companion object {
        @JvmStatic
        fun newInstance() = FriendsFragment()
    }
}

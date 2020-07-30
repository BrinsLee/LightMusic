package com.brins.lightmusic.ui.widget

import androidx.recyclerview.widget.StaggeredGridLayoutManager


class CustomStaggeredGridLayoutManager(i: Int, vertical: Int) :
    StaggeredGridLayoutManager(i, vertical) {
    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return this.isScrollEnabled && super.canScrollVertically()
    }
}

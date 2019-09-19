package com.brins.lightmusic.ui.base.adapter

import android.content.Context

abstract class CommonViewAdapter<T>(context: Context, var layoutId: Int, var list: ArrayList<T>) :
    BaseRecyclerAdapter<T>(context, list) {

    companion object {
        val TAG: String = "TreeRecyclerViewAdapter"
    }

    init {
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return layoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, t: T, position: Int) {
                converted(holder, t, position)
            }

        })
    }

    protected abstract fun converted(holder: ViewHolder, t: T, position: Int)



}
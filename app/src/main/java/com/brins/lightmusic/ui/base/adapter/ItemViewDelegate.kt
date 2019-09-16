package com.brins.lightmusic.ui.base.adapter

interface ItemViewDelegate <T>{
    fun getItemViewLayoutId(): Int

    fun isForViewType(item: T, position: Int): Boolean

    fun convert(holder: ViewHolder, t: T, position: Int)

}
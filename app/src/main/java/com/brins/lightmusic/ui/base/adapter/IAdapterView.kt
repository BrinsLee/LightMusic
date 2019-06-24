package com.brins.lightmusic.ui.base.adapter

interface IAdapterView<T> {
    fun bind(item: T, position: Int)
}
package com.brins.lightmusic.ui.base.adapter

import androidx.collection.SparseArrayCompat

class ItemViewDelegateManager<T> {
    private var delegates: SparseArrayCompat<ItemViewDelegate<T>> = SparseArrayCompat()

    fun getItemViewDelegateCount(): Int {
        return delegates.size()
    }

    fun addDelegate(delegate: ItemViewDelegate<T>?): ItemViewDelegateManager<T> {
        var viewType = delegates.size()
        if (delegate != null) {
            delegates.put(viewType, delegate)
            viewType++
        }
        return this
    }

    fun addDelegate(viewType: Int, delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        if (delegates.get(viewType) != null) {
            throw IllegalArgumentException(
                "An ItemViewDelegate is already registered for the viewType = "
                        + viewType
                        + ". Already registered ItemViewDelegate is "
                        + delegates.get(viewType)
            )
        }
        delegates.put(viewType, delegate)
        return this
    }

    fun removeDelegate(delegate: ItemViewDelegate<T>?): ItemViewDelegateManager<T> {
        if (delegate == null) {
            throw NullPointerException("ItemViewDelegate is null")
        }
        val indexToRemove = delegates.indexOfValue(delegate)

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    fun removeDelegate(itemType: Int): ItemViewDelegateManager<T> {
        val indexToRemove = delegates.indexOfKey(itemType)

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    fun getItemViewType(item: T, position: Int): Int {
        val delegatesCount = delegates.size()
        for (i in delegatesCount - 1 downTo 0) {
            val delegate = delegates.valueAt(i)
            if (delegate.isForViewType(item, position)) {
                return delegates.keyAt(i)
            }
        }
        throw IllegalArgumentException(
            "No ItemViewDelegate added that matches position=$position in data source"
        )
    }

    fun convert(holder: ViewHolder, item: T, position: Int) {
        val delegatesCount = delegates.size()
        for (i in 0 until delegatesCount) {
            val delegate = delegates.valueAt(i)

            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position)
                return
            }
        }
        throw IllegalArgumentException(
            "No ItemViewDelegateManager added that matches position=$position in data source"
        )
    }


    fun getItemViewLayoutId(viewType: Int): Int {
        return delegates.get(viewType)!!.getItemViewLayoutId()
    }

    fun getItemViewType(itemViewDelegate: ItemViewDelegate<T>): Int {
        return delegates.indexOfValue(itemViewDelegate)
    }

    fun getItemViewDelegate(item: T, position: Int): ItemViewDelegate<T> {
        val delegatesCount = delegates.size()
        for (i in delegatesCount - 1 downTo 0) {
            val delegate = delegates.valueAt(i)
            if (delegate.isForViewType(item, position)) {
                return delegate
            }
        }
        throw IllegalArgumentException(
            "No ItemViewDelegate added that matches position=$position in data source"
        )
    }

    fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T>? {
        return delegates.get(viewType)
    }

    fun getItemViewLayoutId(item: T, position: Int): Int {
        return getItemViewDelegate(item, position).getItemViewLayoutId()
    }
}
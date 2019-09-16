package com.brins.lightmusic.ui.base.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerAdapter<T>(var context: Context, var lists: ArrayList<T>) :
    RecyclerView.Adapter<ViewHolder>() {


    private val mItemViewDelegateManager = ItemViewDelegateManager<T>()
    protected var mOnItemClickListener: OnItemClickListener? = null

    fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>): BaseRecyclerAdapter<*> {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    private fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewDelegate: ItemViewDelegate<T>? =
            mItemViewDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate?.getItemViewLayoutId()
        val holder = ViewHolder.createViewHolder(context, parent, layoutId!!)
        onViewHolderCreated(holder, holder.getConvertView())
        setListener(parent, holder, viewType)
        return holder
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setListener(parent: ViewGroup, holder: ViewHolder, viewType: Int) {
        if (mOnItemClickListener != null) {
            holder.getConvertView().setOnClickListener {
                mOnItemClickListener!!.onItemClick(it.tag as Int)
            }

        }
    }

    fun onViewHolderCreated(holder: ViewHolder, itemView: View) {

    }

    fun convert(holder: ViewHolder, t: T) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tag = position
        convert(holder, lists[position])
    }
}
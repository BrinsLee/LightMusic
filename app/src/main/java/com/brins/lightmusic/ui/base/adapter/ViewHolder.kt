package com.brins.lightmusic.ui.base.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ViewHolder(var mContext: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mViews: SparseArray<View> = SparseArray()

    companion object {
        fun createViewHolder(context: Context, view: View): ViewHolder {
            return ViewHolder(context, view)
        }

        fun createViewHolder(context: Context, parent: ViewGroup, layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return ViewHolder(context, itemView)
        }
    }

    fun <T : View> getView(viewId: Int): T {
        var view = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }


    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setText(viewId: Int, text: String): ViewHolder {
        val tv: TextView = getView(viewId)
        tv.text = text
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any): ViewHolder {
        val view = getView<View>(viewId)
        view.setTag(key, tag)
        return this
    }

    fun setTag(viewId: Int, tag: Any): ViewHolder {
        val view = getView<View>(viewId)
        view.tag = tag
        return this
    }

    fun getConvertView(): View {
        return itemView
    }

    fun setImageResource(viewId: Int,url: String?) {
        if (url == null){
            return
        }
        Glide.with(mContext)
            .load(url)
            .into(getView<ImageView>(viewId))
    }
}
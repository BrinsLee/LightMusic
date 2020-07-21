package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.StringBuilder

class MusicDetailAdapter(list: MutableList<Music>) :
    BaseQuickAdapter<Music, BaseViewHolder>(R.layout.item_online_music, list) {


    private var mItemClickListener: OnItemClickListener? = null

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_online_music, parent, false)
        val myViewHolder = ViewHolder(view)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            val musicDetail = list[position]
            val strBuilder = StringBuilder()
            musicDetail.artistBeans?.forEach { strBuilder.append("${it.name} ") }
            holder.artist.text = strBuilder.toString()
            holder.name.text = musicDetail.name
            holder.count.text = "$position"
            if (mItemClickListener != null) {
                holder.layout.setOnClickListener {
                    mItemClickListener!!.onItemClick(it, position)
                }

            }

        }
    }

    fun setData(data: MutableList<Music>) {
        list = data
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val layout = view.findViewById<ConstraintLayout>(R.id.rootLayout)
        val name = view.findViewById<TextView>(R.id.name)
        val artist = view.findViewById<TextView>(R.id.artist)
        val count = view.findViewById<TextView>(R.id.count)
    }*/

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun convert(helper: BaseViewHolder, item: Music) {
        val strBuilder = StringBuilder()
        item.artistBeans?.forEach { strBuilder.append("${it.name} ") }
        helper.setText(R.id.artist, strBuilder)
        helper.setText(R.id.name, item.name)
        helper.setText(R.id.count, "${helper.layoutPosition}")
        if (mItemClickListener != null) {
            helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {
                mItemClickListener!!.onItemClick(it, helper.layoutPosition)
            }

        }
    }

}
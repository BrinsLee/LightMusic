package com.brins.lightmusic.ui.fragment.discovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.banner.Banner
import com.brins.lightmusic.model.onlinemusic.MusicListBean
import com.bumptech.glide.Glide

class DiscoveryAdapter<T>(var type: Int, var listBean: ArrayList<T>) :
    RecyclerView.Adapter<DiscoveryAdapter.ViewHolder>()
    , View.OnClickListener {

    companion object {
        @JvmStatic
        val TYPE_MUSIC_LIST = 1
        val TYPE_BANNER = 2
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (type == TYPE_MUSIC_LIST) R.layout.item_music_list
            else R.layout.item_recycler_banner, parent, false
        )
        val myViewHolder = ViewHolder(view)
        view.setOnClickListener(this)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return listBean.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listBean.isNotEmpty()) {
            //横幅
            if (TYPE_BANNER == type) {
                val banner = listBean[position] as Banner
                Glide.with(BaseApplication.getInstance().baseContext)
                    .load(banner.picUrl)
                    .into(holder.cover)
                holder.itemView.tag = position
            }
            if (TYPE_MUSIC_LIST == type){
                val musicList = listBean[position] as MusicListBean
                holder.title.text = musicList.name
                holder.description.text = musicList.description
                Glide.with(BaseApplication.getInstance().baseContext)
                    .load(musicList.coverImgUrl)
                    .into(holder.cover)
                holder.itemView.tag = position
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onClick(v: View) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取position
            onItemClickListener!!.onItemClick(v, v.tag as Int)
        }
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.cover)
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
    }
}
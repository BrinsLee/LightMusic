package com.brins.lightmusic.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.customview.RoundConstraintLayout
import com.bumptech.glide.Glide

class TreeRecyclerViewAdapter <T>(var list: ArrayList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_local_music,
            parent,
            false
        )
        val holder = SecondViewHolder(itemView)
        val position = holder.adapterPosition
        return holder
    }

    fun setData(arrayList: ArrayList<T>) {
        this.list = arrayList
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list.size != 0) {
            if (list[position] is UserPlayListBean){
                val playlist = (list[position] as UserPlayListBean)
                Glide.with(BaseApplication.getInstance().baseContext)
                    .load(playlist.coverImgUrl).into((holder as TreeRecyclerViewAdapter<*>.SecondViewHolder).imgCover)
                holder.tvName.text = playlist.name
                holder.tvAccount.text = "共${playlist.trackCount}首"

                if (mItemClickListener != null) {
                    holder.itemRoot.setOnClickListener {
                        mItemClickListener!!.onItemClick(position)
                    }


                }
            }
            if(list[position] is OnlineMusic){
                val playlist = (list[position] as OnlineMusic)
                (holder as TreeRecyclerViewAdapter<*>.SecondViewHolder).tvName.text = playlist.nameMusic
                holder.tvAccount.text = playlist.artistBeans!![0].name
                if (mItemClickListener != null) {
                    holder.itemRoot.setOnClickListener {
                        mItemClickListener!!.onItemClick(position)
                    }


                }
            }

        }

    }


    private inner class SecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRoot = itemView.findViewById<RoundConstraintLayout>(R.id.itemRoot)
        val imgCover = itemView.findViewById<ImageView>(R.id.imgCover)
        val tvName = itemView.findViewById<TextView>(R.id.textViewName)
        val tvAccount = itemView.findViewById<TextView>(R.id.textViewArtist)
    }
}
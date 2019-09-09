package com.brins.lightmusic.ui.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.Music
import com.brins.lightmusic.model.onlinemusic.OnlineMusic
import com.brins.lightmusic.model.userplaylist.UserPlayListBean
import com.brins.lightmusic.ui.customview.RoundConstraintLayout
import com.bumptech.glide.Glide
import java.lang.reflect.ParameterizedType

class TreeRecyclerViewAdapter<T>(var list: ArrayList<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TAG: String = "TreeRecyclerViewAdapter"
    }

    private var mItemClickListener: OnItemClickListener? = null
    private var className = ""

    init {
        declareType()
    }

    private fun declareType() {
        if (list.isNotEmpty()) {
            if (list[0] is UserPlayListBean) {
                className = "UserPlayListBean"

            }
            if (list[0] is Music) {
                className = "OnlineMusic"
            }
        }
        Log.d(TAG, className)

    }

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
        if (mItemClickListener != null) {
            itemView.setOnClickListener {
                mItemClickListener!!.onItemClick(it.tag as Int)
            }

        }
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
            holder.itemView.tag = position
            if (className == "UserPlayListBean") {
                val playlist = (list[position] as UserPlayListBean)
                Glide.with(BaseApplication.getInstance().baseContext)
                    .load(playlist.coverImgUrl)
                    .into((holder as TreeRecyclerViewAdapter<*>.SecondViewHolder).imgCover)
                holder.tvName.text = playlist.name
                holder.tvAccount.text = "共${playlist.trackCount}首"
            }
            else{
                val playlist = (list[position] as Music)
                Glide.with(BaseApplication.getInstance().baseContext)
                    .load(playlist.album.picUrl)
                    .into((holder as TreeRecyclerViewAdapter<*>.SecondViewHolder).imgCover)
                (holder as TreeRecyclerViewAdapter<*>.SecondViewHolder).tvName.text =
                    playlist.name
                holder.tvAccount.text = playlist.artistBeans!![0].name
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
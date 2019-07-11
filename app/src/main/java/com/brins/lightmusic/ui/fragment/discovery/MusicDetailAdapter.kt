package com.brins.lightmusic.ui.fragment.discovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.R
import com.brins.lightmusic.model.OnlineMusic
import org.w3c.dom.Text
import java.lang.StringBuilder

class MusicDetailAdapter (var context: Context, var list: MutableList<OnlineMusic>) :
    RecyclerView.Adapter<MusicDetailAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_online_music,parent,false)
        val myViewHolder = ViewHolder(view)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list.isNotEmpty()){
            val musicDetail = list[position]
            val strBuilder = StringBuilder()
            musicDetail.artists?.forEach { strBuilder.append("${it.name},") }
            holder.artist.text = strBuilder.toString()
            holder.name.text = musicDetail.name
            holder.count.text = "$position"
        }
    }

    fun setData(data: MutableList<OnlineMusic>) {
        list = data
    }

    class ViewHolder (var view : View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.name)
        val artist = view.findViewById<TextView>(R.id.artist)
        val count = view.findViewById<TextView>(R.id.count)
    }
}
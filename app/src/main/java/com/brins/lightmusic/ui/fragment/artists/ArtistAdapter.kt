package com.brins.lightmusic.ui.fragment.artists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.artist.ArtistBean
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide

class ArtistAdapter(var list: ArrayList<ArtistBean>) :
    RecyclerView.Adapter<ArtistAdapter.viewHolder>() {


    private var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_local_music, parent, false)
        val viewHolder = viewHolder(view)
        if (mItemClickListener != null) {
            view.setOnClickListener {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mItemClickListener!!.onItemClick(position)
                }
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        if (list.size != 0) {
            holder.itemView.tag = position
            val artist = list[position]
            holder.textViewName.text = artist.name
            holder.textViewArtist.text =
                "${artist.musicSize} ${BaseApplication.getInstance().baseContext.getString(R.string.num_songs)}"
            Glide.with(BaseApplication.getInstance().baseContext).load(artist.picUrl)
                .into(holder.imgCover)
        }
    }


    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textViewName: TextView
        var textViewArtist: TextView
        var imgCover: ImageView

        init {
            textViewName = view.findViewById(R.id.textViewName)
            textViewArtist = view.findViewById(R.id.textViewArtist)
            imgCover = view.findViewById(R.id.imgCover)

        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

}
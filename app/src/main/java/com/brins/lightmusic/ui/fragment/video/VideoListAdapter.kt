package com.brins.lightmusic.ui.fragment.video

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brins.lightmusic.BaseApplication
import com.brins.lightmusic.R
import com.brins.lightmusic.model.musicvideo.Mv
import com.brins.lightmusic.ui.base.adapter.OnItemClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView

class VideoListAdapter(var MvData: MutableList<Mv>, var context: Context) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private var requestOptions = RequestOptions().override(100, 100).centerCrop()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_video_list, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return MvData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (MvData.isNotEmpty()) {
            val mv = MvData[position].dataBean
            holder.title.text = mv.name
            holder.nickName.text = mv.artistName
            holder.watchCount.text = if (mv.playCount > 10000) "${mv.playCount / 10000}万播放" else {
                "${mv.playCount}播放"
            }
            Glide.with(BaseApplication.getInstance()).load(mv.cover)
                .into(holder.videoPlayer)
            Glide.with(BaseApplication.getInstance())
                .load(mv.cover)
                .apply(requestOptions)
                .into(holder.avatar)

            if (mItemClickListener != null) {
                holder.container.setOnClickListener {
                    mItemClickListener!!.onItemClick(it, position)
                }
            }
        }

    }

    fun setOnItemListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.tv_title)
        var container: LinearLayout = view.findViewById(R.id.container)
        var videoPlayer: ImageView = view.findViewById(R.id.video_player)
        var watchCount: TextView = view.findViewById(R.id.tv_watch_count)
        var avatar: RoundedImageView = view.findViewById(R.id.iv_avatar)
        var nickName: TextView = view.findViewById(R.id.tv_author)
    }
}